<script setup>
import { ref, onMounted } from 'vue'
import { Register, SendEmailVerify, Login, GetPicCode } from '../api';
import { ElMessage } from 'element-plus';
import { useUserStore } from '../store/userStore';

const userStore = useUserStore()
const emit = defineEmits(['closeLoginView'])
const isSigIn = ref(true)
const imageBase64 = ref("")
const sendEmailState = ref(false)
const sendEmailText = ref("邮箱验证")
const loginForm = ref({
    email: '',
    username: '',
    password: '',
    againPwd: '',
    picCode: '',
    timestamp: Date.now(),
})

// 获取图片验证码
const getPicCode = () => {
    loginForm.value.timestamp = Date.now()
    GetPicCode(loginForm.value.timestamp).then(res => {
        imageBase64.value = res
    })
}

// 用户注册
const register = () => {
    if (!/^.{1,8}$/.test(loginForm.value.username)) {
        ElMessage.warning("用户昵称长度的范围必须在 1 ~ 8")
        return;
    }
    if (!/^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/.test(loginForm.value.email)) {
        ElMessage.warning("邮箱不符合规范")
        return;
    }
    if (!/^.{6,12}$/.test(loginForm.value.password)) {
        ElMessage.warning("用户密码长度的范围必须在 6 ~ 12")
        return;
    }
    if (!/^.{6,12}$/.test(loginForm.value.againPwd)) {
        ElMessage.warning("用户二次密码长度的范围必须在 6 ~ 12")
        return;
    }
    if (loginForm.value.password !== loginForm.value.againPwd) {
        ElMessage.warning("两次密码不一致  ")
        return;
    }
    const data = {
        username: loginForm.value.username,
        password: loginForm.value.password,
        againPwd: loginForm.value.againPwd,
        email: loginForm.value.email
    }
    Register(data).then(res => {
        ElMessage.succes("注册成功")
    })
}
// 倒计时
const countDown = () => {
    sendEmailState.value = true
    var n = 60
    const timer = setInterval(() => {
        if (n === 0) {
            clearInterval(timer);
            sendEmailText.value = "邮箱验证";
            sendEmailState.value = false
        } else {
            n--;
            sendEmailText.value = n + "秒后重新获取";
        }
    }, 1000);
}
// 发送邮箱验证
const sendEmailVerify = () => {
    if (sendEmailState.value) {
        ElMessage.warning(`邮箱已发送，请${sendEmailText.value}`)
        return;
    }
    if (loginForm.value.picCode.length !== 4) {
        ElMessage.warning("验证码不符合规范")
        return;
    }
    if (!/^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/.test(loginForm.value.email)) {
        ElMessage.warning("邮箱不符合规范")
        return;
    }
    const data = {
        picCode: loginForm.value.picCode,
        email: loginForm.value.email,
        timestamp: loginForm.value.timestamp
    }
    SendEmailVerify(data).then(res => {
        ElMessage.success(res)
        // 发送成功，60秒后才能发送
        countDown()
    })
}
// 用户登录
const login = () => {
    if (!/^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/.test(loginForm.value.email)) {
        ElMessage.warning("邮箱不符合规范")
        return;
    }
    if (!/^.{6,12}$/.test(loginForm.value.password)) {
        ElMessage.warning("用户密码长度的范围必须在 6 ~ 12")
        return;
    }
    const data = {
        password: loginForm.value.password,
        email: loginForm.value.email
    }
    Login(data).then(res => {
        ElMessage.success("登陆成功")
        // 保存 token 在本地
        localStorage.setItem("token", res.token)
        // 保存用户信息
        userStore.setUser(res.user)
        emit('closeLoginView')
    })
}

onMounted(() => [
    getPicCode()
])
</script>

<template>
    <div class="fixed -translate-y-1/2 -translate-x-1/2 top-1/2 left-1/2 transform z-10">
        <div class="flex flex-col md:flex-row border-2 w-[340px] md:w-[650px] h-auto rounded-lg shadow-lg">
            <div @click="emit('closeLoginView')"
                class="hover:bg-blue-300 rounded-full cursor-pointer absolute right-1 top-1">
                <svg width="24" height="24" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M14 14L34 34" stroke="#333" stroke-width="4" stroke-linecap="round"
                        stroke-linejoin="round" />
                    <path d="M14 34L34 14" stroke="#333" stroke-width="4" stroke-linecap="round"
                        stroke-linejoin="round" />
                </svg>
            </div>
            <img class="w-64 h-96 rounded-l-lg hidden md:block" src="../assets/login.jpg" alt="">
            <div v-if="isSigIn" class="flex-1 h-96 flex flex-col justify-center items-center bg-white rounded-r-lg">
                <div class="text-center my-4">
                    <div class="text-blue-400 text-4xl my-1">Welcome!</div>
                    <div class="text-slate-400">Sign in you Account</div>
                </div>
                <div class="flex flex-col items-center gap-4 mb-4">
                    <input type="text" v-model="loginForm.email"
                        class="text-sm rounded-md border-2 w-60 h-9 p-2 border-gray-200 focus:border-blue-500 box-border outline-none"
                        placeholder="Eamil" />
                    <input type="password" v-model="loginForm.password"
                        class="text-sm rounded-md border-2 w-60 h-9 p-2 border-b-gray-200 focus:border-blue-500 box-border outline-none"
                        placeholder="Password" />
                </div>
                <div class="text-blue-950 text-xs my-4 flex justify-center gap-8">
                    <div class="flex gap-1"><input type="checkbox" name="vehicle" value=""
                            class="cursor-pointer"><label>Remember me</label></div>
                    <a class="text-blue-500 underline cursor-pointer">Forgot Password?</a>
                </div>
                <div class="flex gap-4 justify-center my-4">
                    <button class="border w-36 border-white bg-indigo-500 text-white py-1 px-4 rounded-3xl"
                        @click="login">SIGN
                        IN</button>
                    <button class="border w-36 border-indigo-500 text-indigo-500 py-1 px-4 rounded-3xl"
                        @click="isSigIn = false">登录</button>
                </div>
            </div>
            <div v-else class="flex-1 h-96 bg-white rounded-r-lg flex flex-col justify-center items-center">
                <div class=" text-center">
                    <div class="text-blue-400 text-4xl my-1">注册</div>
                </div>
                <div class="flex flex-wrap justify-center items-center gap-4 my-4">
                    <input type="text" v-model="loginForm.email"
                        class="text-sm rounded-md border-2 w-60 md:w-44 h-8 p-2 border-b-gray-200 focus:border-blue-500 box-border outline-none"
                        placeholder="Email" />
                    <input type="text" v-model="loginForm.username"
                        class="text-sm rounded-md border-2 w-60 md:w-44 h-8 p-2 border-gray-200 focus:border-blue-500 box-border outline-none"
                        placeholder="Username" />
                    <input type="password" v-model="loginForm.password"
                        class="text-sm rounded-md border-2 w-60 md:w-44 h-8 p-2 border-b-gray-200 focus:border-blue-500 box-border outline-none"
                        placeholder="Password" />
                    <input type="password" v-model="loginForm.againPwd"
                        class="text-sm rounded-md border-2 w-60 md:w-44 h-8 p-2 border-b-gray-200 focus:border-blue-500 box-border outline-none"
                        placeholder="Again Password" />
                </div>
                <div class="flex justify-center items-center gap-4 px-3">
                    <el-tooltip content="点击切换图片" placement="bottom" effect="light">
                        <img class="w-20 cursor-pointer" :src="`data:image/png;base64,${imageBase64}`"
                            @click="getPicCode" />
                    </el-tooltip>

                    <input type="text" v-model="loginForm.picCode"
                        class="text-sm rounded-md border-2 w-24 h-8 p-2 border-b-gray-200 focus:border-blue-500 box-border outline-none"
                        placeholder="Code" />
                    <button class="w-44 h-8 rounded-md felx justify-center items-center border border-blue-500"
                        @click="sendEmailVerify">{{ sendEmailText }}</button>
                </div>
                <div class="text-blue-950 text-xs my-4 flex justify-center gap-8">
                    <div class="flex gap-1"><input type="checkbox" name="vehicle" value=""
                            class="cursor-pointer"><label>遵守不平等协议</label></div>
                </div>
                <div class="flex gap-4 flex-col items-center my-4">
                    <button class="border w-60 text-white bg-indigo-500 py-1 px-4 rounded-3xl" @click="register">注册</button>
                    <div class="text-blue-950 text-xs">
                        您已有账号? <a class="text-blue-500 underline cursor-pointer"
                            @click="isSigIn = true">返回登录</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped></style>