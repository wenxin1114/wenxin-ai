import axios from 'axios'
import { ElMessage } from 'element-plus'

const service = axios.create({
    // baseURL: 'https://d73a959.r17.cpolar.top/api/',
	baseURL: 'http://localhost:8080/api',
	timeout: 300000
})

// 请求拦截器
service.interceptors.request.use(
	(config) => {
		config.headers.token = localStorage.getItem("token") || ''
		return config // 必须返回配置
	},
	(error) => {
		return Promise.reject(error)
	}
)

// 响应拦截器
// 响应拦截器
service.interceptors.response.use(
    (response) => {
        const { code, message, data } = response.data;
        // 要根据code的成功与否决定下面的操作
        if (code === 200) {
            return data;
        } else if (code === 50) {
            localStorage.removeItem("token")
            ElMessage.error("登录失效");
            
        } else if (code === 52) {
            ElMessage.error("未登录")
        } else {
            ElMessage.error(message);
            return Promise.reject(new Error(message));
        }
    },
    (error) => {
        // 请求异常处理逻辑
        console.error("请求出现异常:", error);
        ElMessage.error("请求出现异常，请稍后重试");
        return Promise.reject(error);
    }
);
export default service
