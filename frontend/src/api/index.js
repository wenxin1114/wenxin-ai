import request from '../utils/request'

// 用户注册
export const Register = (data) => {
    return request({
        url: '/user/register',
        method: 'post',
        data
    })
}

// 用户登录
export const Login = (data) => {
    return request({
        url: '/user/login',
        method: 'post',
        data
    })
}

// 发送邮箱验证
export const SendEmailVerify  = (data) => {
    return request({
        url: '/common/email/send',
        method: 'post',
        data
    })
}

// 获取用户信息
export const GetUserInfo = () => {
    return request({
        url: '/user/info',
    })
}

// 发送Chat信息
export const SendMessage = (data) => {
    return request({
        url: '/model/chat',
        method: 'post',
        data
    })
} 

// 推出登录
export const Logout = () => {
    return request({
        url: '/user/logout'
    })
}

// 获取图片验证码
export const GetPicCode = (timestamp) => {
    return request({
        url: `/common/code/pic?timestamp=${timestamp}`
    })
}

// 修改密码
export const EditPassword = (data) => {
    return request({
        url: '/user/pwd/edit',
        method: 'post',
        data
    })

}