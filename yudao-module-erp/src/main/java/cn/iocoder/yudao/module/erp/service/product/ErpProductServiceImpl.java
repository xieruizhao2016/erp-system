package cn.iocoder.yudao.module.erp.service.product;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductCategoryDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductUnitDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuRelationDO;
import cn.iocoder.yudao.module.erp.dal.mysql.product.ErpProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.PRODUCT_NOT_ENABLE;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.PRODUCT_NOT_EXISTS;

/**
 * ERP 产品 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class ErpProductServiceImpl implements ErpProductService {

    @Resource
    private ErpProductMapper productMapper;

    @Resource
    private ErpProductCategoryService productCategoryService;
    @Resource
    private ErpProductUnitService productUnitService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.productsku.ProductSkuService productSkuService;
    @Resource
    private cn.iocoder.yudao.module.erp.dal.mysql.productsku.ProductSkuRelationMapper productSkuRelationMapper;
    @Resource
    private cn.iocoder.yudao.module.erp.service.productpackage.ProductPackageService productPackageService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.productoem.ProductOemService productOemService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProduct(ProductSaveReqVO createReqVO) {
        // 校验分类
        ErpProductCategoryDO category = productCategoryService.getProductCategory(createReqVO.getCategoryId());
        if (category == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        // 插入产品
        ErpProductDO product = BeanUtils.toBean(createReqVO, ErpProductDO.class);
        productMapper.insert(product);
        // 处理SKU关联关系
        if (CollUtil.isNotEmpty(createReqVO.getSkuIds())) {
            log.info("创建产品时关联SKU: productId={}, skuIds={}", product.getId(), createReqVO.getSkuIds());
            saveProductSkuRelations(product.getId(), createReqVO.getSkuIds());
        } else {
            log.info("创建产品时未关联SKU: productId={}, skuIds为空", product.getId());
        }
        // 返回
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(ProductSaveReqVO updateReqVO) {
        // 校验分类
        ErpProductCategoryDO category = productCategoryService.getProductCategory(updateReqVO.getCategoryId());
        if (category == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
        // 校验存在
        validateProductExists(updateReqVO.getId());
        // 更新产品
        ErpProductDO updateObj = BeanUtils.toBean(updateReqVO, ErpProductDO.class);
        productMapper.updateById(updateObj);
        // 处理SKU关联关系
        updateProductSkuRelations(updateReqVO.getId(), updateReqVO.getSkuIds());
    }

    @Override
    public void deleteProduct(Long id) {
        // 校验存在
        validateProductExists(id);
        // 删除
        productMapper.deleteById(id);
    }

    @Override
    public List<ErpProductDO> validProductList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpProductDO> list = productMapper.selectByIds(ids);
        Map<Long, ErpProductDO> productMap = convertMap(list, ErpProductDO::getId);
        for (Long id : ids) {
            ErpProductDO product = productMap.get(id);
            if (productMap.get(id) == null) {
                throw exception(PRODUCT_NOT_EXISTS);
            }
            if (CommonStatusEnum.isDisable(product.getStatus())) {
                throw exception(PRODUCT_NOT_ENABLE, product.getName());
            }
        }
        return list;
    }

    private void validateProductExists(Long id) {
        if (productMapper.selectById(id) == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
    }

    @Override
    public ErpProductDO getProduct(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    public List<ErpProductRespVO> getProductVOListByStatus(Integer status) {
        List<ErpProductDO> list = productMapper.selectListByStatus(status);
        return buildProductVOList(list);
    }

    @Override
    public List<ErpProductRespVO> getProductVOList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<ErpProductDO> list = productMapper.selectByIds(ids);
        return buildProductVOList(list);
    }

    @Override
    public PageResult<ErpProductRespVO> getProductVOPage(ErpProductPageReqVO pageReqVO) {
        PageResult<ErpProductDO> pageResult = productMapper.selectPage(pageReqVO);
        return new PageResult<>(buildProductVOList(pageResult.getList()), pageResult.getTotal());
    }

    private List<ErpProductRespVO> buildProductVOList(List<ErpProductDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 获取分类和单位Map
        Map<Long, ErpProductCategoryDO> categoryMap = productCategoryService.getProductCategoryMap(
                convertSet(list, ErpProductDO::getCategoryId));
        Map<Long, ErpProductUnitDO> unitMap = productUnitService.getProductUnitMap(
                convertSet(list, ErpProductDO::getUnitId));
        
        // 获取包装、OEM的编码Map
        Map<Long, String> packageCodeMap = convertMap(
                productPackageService.getProductPackageList(convertSet(list, ErpProductDO::getPackageId)),
                cn.iocoder.yudao.module.erp.dal.dataobject.productpackage.ProductPackageDO::getId,
                cn.iocoder.yudao.module.erp.dal.dataobject.productpackage.ProductPackageDO::getPackageCode);
        Map<Long, String> oemCodeMap = convertMap(
                productOemService.getProductOemList(convertSet(list, ErpProductDO::getOemId)),
                cn.iocoder.yudao.module.erp.dal.dataobject.productoem.ProductOemDO::getId,
                cn.iocoder.yudao.module.erp.dal.dataobject.productoem.ProductOemDO::getOemCode);
        
        // 批量获取产品的SKU列表
        Set<Long> productIds = convertSet(list, ErpProductDO::getId);
        Map<Long, List<cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO>> productSkuMap = 
                getProductSkuMapByProductIds(productIds);
        
        return BeanUtils.toBean(list, ErpProductRespVO.class, product -> {
            // 设置分类和单位名称
            MapUtils.findAndThen(categoryMap, product.getCategoryId(),
                    category -> product.setCategoryName(category.getName()));
            MapUtils.findAndThen(unitMap, product.getUnitId(),
                    unit -> product.setUnitName(unit.getName()));
            
            // 设置包装、OEM编码
            if (product.getPackageId() != null) {
                product.setPackageCode(packageCodeMap.get(product.getPackageId()));
            }
            if (product.getOemId() != null) {
                product.setOemCode(oemCodeMap.get(product.getOemId()));
            }
            
            // 设置SKU列表（只包含启用的SKU）
            List<cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO> skuList = 
                    productSkuMap.get(product.getId());
            if (CollUtil.isNotEmpty(skuList)) {
                product.setSkuList(BeanUtils.toBean(skuList, 
                        cn.iocoder.yudao.module.erp.controller.admin.productsku.vo.ProductSkuRespVO.class));
            }
        });
    }
    
    /**
     * 批量获取产品的SKU列表（只包含启用的SKU）
     *
     * @param productIds 产品ID集合
     * @return 产品ID -> SKU列表的映射
     */
    private Map<Long, List<cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO>> getProductSkuMapByProductIds(
            Set<Long> productIds) {
        if (CollUtil.isEmpty(productIds)) {
            return Collections.emptyMap();
        }
        // 批量查询关联关系
        List<ProductSkuRelationDO> relations = productSkuRelationMapper.selectListByProductIds(productIds);
        if (CollUtil.isEmpty(relations)) {
            return Collections.emptyMap();
        }
        // 获取所有SKU ID
        Set<Long> skuIds = convertSet(relations, ProductSkuRelationDO::getSkuId);
        // 批量查询SKU
        List<cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO> skuList = 
                productSkuService.getProductSkuList(skuIds);
        // 过滤启用的SKU（CommonStatusEnum.ENABLE = 0）
        List<cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO> enabledSkuList = skuList.stream()
                .filter(sku -> sku.getStatus() != null && sku.getStatus() == 0)
                .collect(java.util.stream.Collectors.toList());
        // 按产品ID分组
        Map<Long, List<cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO>> skuMap = 
                new java.util.HashMap<>();
        Map<Long, cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO> skuIdMap = 
                convertMap(enabledSkuList, cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO::getId);
        for (ProductSkuRelationDO relation : relations) {
            cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO sku = skuIdMap.get(relation.getSkuId());
            if (sku != null) {
                skuMap.computeIfAbsent(relation.getProductId(), k -> new java.util.ArrayList<>()).add(sku);
            }
        }
        // 对每个产品的SKU列表进行排序
        for (List<cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO> skus : skuMap.values()) {
            skus.sort(java.util.Comparator
                    .comparing(cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO::getSort, 
                            java.util.Comparator.nullsLast(Integer::compareTo))
                    .thenComparing(cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO::getId, 
                            java.util.Comparator.reverseOrder()));
        }
        return skuMap;
    }

    @Override
    public Long getProductCountByCategoryId(Long categoryId) {
        return productMapper.selectCountByCategoryId(categoryId);
    }

    @Override
    public Long getProductCountByUnitId(Long unitId) {
        return productMapper.selectCountByUnitId(unitId);
    }

    /**
     * 保存产品SKU关联关系
     *
     * @param productId 产品ID
     * @param skuIds SKU ID列表
     */
    private void saveProductSkuRelations(Long productId, List<Long> skuIds) {
        if (CollUtil.isEmpty(skuIds)) {
            log.warn("saveProductSkuRelations: skuIds为空, productId={}", productId);
            return;
        }
        log.info("saveProductSkuRelations: 开始保存产品SKU关联, productId={}, skuIds={}", productId, skuIds);
        // 查询已存在的关联关系
        List<ProductSkuRelationDO> existingRelations = productSkuRelationMapper.selectListByProductId(productId);
        Set<Long> existingSkuIdsSet = convertSet(existingRelations, ProductSkuRelationDO::getSkuId);
        List<Long> existingSkuIds = new ArrayList<>(existingSkuIdsSet);
        log.info("saveProductSkuRelations: 已存在的关联关系数量={}, existingSkuIds={}", existingRelations.size(), existingSkuIds);
        
        // 过滤出需要新增的SKU
        List<Long> toAdd = skuIds.stream()
                .filter(skuId -> !existingSkuIds.contains(skuId))
                .collect(java.util.stream.Collectors.toList());
        
        if (CollUtil.isEmpty(toAdd)) {
            log.warn("saveProductSkuRelations: 没有需要新增的SKU关联, productId={}", productId);
            return;
        }
        
        log.info("saveProductSkuRelations: 准备新增{}个SKU关联, toAdd={}", toAdd.size(), toAdd);
        // 批量插入关联关系（在插入前再次检查，避免并发或查询问题导致的重复）
        List<ProductSkuRelationDO> relations = new ArrayList<>();
        for (Long skuId : toAdd) {
            // 再次检查是否已存在（防止并发问题）
            List<ProductSkuRelationDO> existing = productSkuRelationMapper.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductSkuRelationDO>()
                            .eq(ProductSkuRelationDO::getProductId, productId)
                            .eq(ProductSkuRelationDO::getSkuId, skuId));
            if (CollUtil.isEmpty(existing)) {
                ProductSkuRelationDO relation = ProductSkuRelationDO.builder()
                        .productId(productId)
                        .skuId(skuId)
                        .build();
                relations.add(relation);
            } else {
                log.warn("saveProductSkuRelations: SKU关联已存在，跳过插入, productId={}, skuId={}", productId, skuId);
            }
        }
        if (CollUtil.isNotEmpty(relations)) {
            Boolean result = productSkuRelationMapper.insertBatch(relations);
            log.info("saveProductSkuRelations: 批量插入完成, productId={}, 插入数量={}, 结果={}", productId, relations.size(), result);
        } else {
            log.info("saveProductSkuRelations: 所有SKU关联都已存在，无需插入, productId={}", productId);
        }
    }

    /**
     * 更新产品SKU关联关系
     *
     * @param productId 产品ID
     * @param skuIds SKU ID列表
     */
    private void updateProductSkuRelations(Long productId, List<Long> skuIds) {
        log.info("updateProductSkuRelations: 开始更新产品SKU关联, productId={}, skuIds={}", productId, skuIds);
        // 查询现有的关联关系
        List<ProductSkuRelationDO> existingRelations = productSkuRelationMapper.selectListByProductId(productId);
        Set<Long> existingSkuIdsSet = convertSet(existingRelations, ProductSkuRelationDO::getSkuId);
        List<Long> existingSkuIds = new ArrayList<>(existingSkuIdsSet);
        log.info("updateProductSkuRelations: 已存在的关联关系数量={}, existingSkuIds={}", existingRelations.size(), existingSkuIds);
        
        // 计算需要新增和删除的SKU
        List<Long> newSkuIds = CollUtil.isEmpty(skuIds) ? Collections.emptyList() : skuIds;
        // 找出需要新增的SKU（在新列表中但不在现有列表中）
        List<Long> toAdd = newSkuIds.stream()
                .filter(skuId -> !existingSkuIds.contains(skuId))
                .collect(java.util.stream.Collectors.toList());
        // 找出需要删除的SKU（在现有列表中但不在新列表中）
        List<Long> toRemove = existingSkuIds.stream()
                .filter(skuId -> !newSkuIds.contains(skuId))
                .collect(java.util.stream.Collectors.toList());
        
        log.info("updateProductSkuRelations: 需要新增的SKU数量={}, toAdd={}, 需要删除的SKU数量={}, toRemove={}", 
                toAdd.size(), toAdd, toRemove.size(), toRemove);
        
        // 删除关联关系
        if (CollUtil.isNotEmpty(toRemove)) {
            for (Long skuId : toRemove) {
                productSkuRelationMapper.deleteByProductIdAndSkuId(productId, skuId);
            }
            log.info("updateProductSkuRelations: 已删除{}个SKU关联关系", toRemove.size());
        }
        
        // 新增关联关系（在插入前再次检查，避免并发或查询问题导致的重复）
        if (CollUtil.isNotEmpty(toAdd)) {
            List<ProductSkuRelationDO> relations = new ArrayList<>();
            for (Long skuId : toAdd) {
                // 再次检查是否已存在（防止并发问题）
                List<ProductSkuRelationDO> existing = productSkuRelationMapper.selectList(
                        new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductSkuRelationDO>()
                                .eq(ProductSkuRelationDO::getProductId, productId)
                                .eq(ProductSkuRelationDO::getSkuId, skuId));
                if (CollUtil.isEmpty(existing)) {
                    ProductSkuRelationDO relation = ProductSkuRelationDO.builder()
                            .productId(productId)
                            .skuId(skuId)
                            .build();
                    relations.add(relation);
                } else {
                    log.warn("updateProductSkuRelations: SKU关联已存在，跳过插入, productId={}, skuId={}", productId, skuId);
                }
            }
            if (CollUtil.isNotEmpty(relations)) {
                Boolean result = productSkuRelationMapper.insertBatch(relations);
                log.info("updateProductSkuRelations: 批量插入完成, productId={}, 插入数量={}, 结果={}", productId, relations.size(), result);
            } else {
                log.info("updateProductSkuRelations: 所有SKU关联都已存在，无需插入, productId={}", productId);
            }
        }
    }

}
