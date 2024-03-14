import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';

import "tailwindcss/tailwind.css"
import 'element-plus/dist/index.css'
import '@kangc/v-md-editor/lib/style/preview.css';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import './style.scss'
import 'animate.css'

import createCopyCodePreview from '@kangc/v-md-editor/lib/plugins/copy-code/preview';
import '@kangc/v-md-editor/lib/plugins/copy-code/copy-code.css';
// highlightjs
import hljs from 'highlight.js';

VMdPreview.use(githubTheme, {
  Hljs: hljs,
});
VMdPreview.use(createCopyCodePreview())


const app = createApp(App)
const pinia = createPinia()
app.use(pinia)
app.use(VMdPreview)
app.mount('#app')