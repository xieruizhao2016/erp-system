import {resolve} from 'path'
import type {ConfigEnv, UserConfig} from 'vite'
import {loadEnv} from 'vite'
import {createVitePlugins} from './build/vite'
import {exclude, include} from "./build/vite/optimize"
// 当前执行node命令时文件夹的地址(工作目录)
const root = process.cwd()

// 路径查找
function pathResolve(dir: string) {
    return resolve(root, '.', dir)
}

// https://vitejs.dev/config/
export default ({command, mode}: ConfigEnv): UserConfig => {
    let env = {} as any
    const isBuild = command === 'build'
    if (!isBuild) {
        env = loadEnv((process.argv[3] === '--mode' ? process.argv[4] : process.argv[3]), root)
    } else {
        env = loadEnv(mode, root)
    }
    return {
        base: env.VITE_BASE_PATH,
        root: root,
        // 服务端渲染
        server: {
            port: env.VITE_PORT, // 端口号
            host: "0.0.0.0",
            open: env.VITE_OPEN === 'true',
            // 本地跨域代理. 目前注释的原因：暂时没有用途，server 端已经支持跨域
            // proxy: {
            //   ['/admin-api']: {
            //     target: env.VITE_BASE_URL,
            //     ws: false,
            //     changeOrigin: true,
            //     rewrite: (path) => path.replace(new RegExp(`^/admin-api`), ''),
            //   },
            // },
        },
        // 项目使用的vite插件。 单独提取到build/vite/plugin中管理
        plugins: createVitePlugins(),
        css: {
            preprocessorOptions: {
                scss: {
                    additionalData: '@use "@/styles/variables.scss" as *;',
                    javascriptEnabled: true,
                    silenceDeprecations: ["legacy-js-api"], // 参考自 https://stackoverflow.com/questions/78997907/the-legacy-js-api-is-deprecated-and-will-be-removed-in-dart-sass-2-0-0
                }
            }
        },
        resolve: {
            extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.scss', '.css'],
            alias: [
                {
                    find: 'vue-i18n',
                    replacement: 'vue-i18n/dist/vue-i18n.cjs.js'
                },
                {
                    find: /\@\//,
                    replacement: `${pathResolve('src')}/`
                }
            ]
        },
        build: {
            // 使用 esbuild 替代 terser，构建速度提升 10-20 倍
            // 如需更好的压缩率，可通过环境变量 VITE_MINIFY=terser 切换回 terser
            minify: env.VITE_MINIFY === 'terser' ? 'terser' : 'esbuild',
            outDir: env.VITE_OUT_DIR || 'dist',
            sourcemap: env.VITE_SOURCEMAP === 'true' ? 'inline' : false,
            // 启用并行构建，充分利用多核 CPU
            target: 'es2015',
            // 增大 chunk 大小警告阈值，减少不必要的警告
            chunkSizeWarningLimit: 1000,
            // 优化构建性能
            cssCodeSplit: true,
            // terser 压缩选项（仅在 VITE_MINIFY=terser 时生效）
            ...(env.VITE_MINIFY === 'terser' ? {
                terserOptions: {
                    compress: {
                        drop_debugger: env.VITE_DROP_DEBUGGER === 'true',
                        drop_console: env.VITE_DROP_CONSOLE === 'true'
                    }
                }
            } : {
                // esbuild 压缩选项（默认使用）
                esbuild: {
                    drop: [
                        ...(env.VITE_DROP_DEBUGGER === 'true' ? ['debugger'] : []),
                        ...(env.VITE_DROP_CONSOLE === 'true' ? ['console'] : [])
                    ]
                }
            }),
            rollupOptions: {
                output: {
                    manualChunks: {
                      echarts: ['echarts'], // 将 echarts 单独打包，参考 https://gitee.com/yudaocode/yudao-ui-admin-vue3/issues/IAB1SX 讨论
                      'form-create': ['@form-create/element-ui'], // 参考 https://github.com/yudaocode/yudao-ui-admin-vue3/issues/148 讨论
                      'form-designer': ['@form-create/designer'],
                    }
                },
            },
        },
        optimizeDeps: {include, exclude}
    }
}
