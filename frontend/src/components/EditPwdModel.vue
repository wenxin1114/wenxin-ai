<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '../store/userStore.js'
import { EditPassword, GetPicCode, SendEmailVerify } from '../api/index.js';
import { ElMessage } from 'element-plus';

const emit = defineEmits(['closeEditPwdView'])
const userStore = useUserStore()
const imageBase64 = ref("")
const sendEmailState = ref(false)
const sendEmailText = ref("邮箱验证")
const user = computed(() => {
    return userStore.user;
})
const editPwdForm = ref({
    oldPassword: '',
    newPassword: ''
})
const sendEmailForm = ref({
    picCode: '',
    timestamp: null,
    email: user.value.email
})

// 修改密码
const editPassword = () => {
    EditPassword(editPwdForm.value).then(res => {
        ElMessage.success(res)
    })
}


// 获取图片验证码
const getPicCode = () => {
    sendEmailForm.value.timestamp = Date.now()
    GetPicCode(sendEmailForm.value.timestamp).then(res => {
        imageBase64.value = res
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
    if (sendEmailForm.value.picCode.length !== 4) {
        ElMessage.warning("验证码不符合规范")
        return;
    }
    if (!/^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/.test(sendEmailForm.value.email)) {
        ElMessage.warning("邮箱不符合规范")
        return;
    }
    SendEmailVerify(sendEmailForm.value).then(res => {
        ElMessage.success(res)
        // 发送成功，60秒后才能发送
        countDown()
    })
}

onMounted(() => {
    getPicCode()
})
</script>

<template>
    <div
        class="fixed -translate-y-1/2 -translate-x-1/2 top-1/2 left-1/2 transform z-20 border-2 p-4 bg-white shadow-lg">
        <div @click="emit('closeEditPwdView')"
            class="hover:bg-blue-300 rounded-full cursor-pointer absolute right-1 top-1">
            <svg width="24" height="24" viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M14 14L34 34" stroke="#333" stroke-width="4" stroke-linecap="round" stroke-linejoin="round" />
                <path d="M14 34L34 14" stroke="#333" stroke-width="4" stroke-linecap="round" stroke-linejoin="round" />
            </svg>
        </div>
        <div class="text-center">
            修改密码
        </div>
        <div class="flex flex-col justify-center items-center gap-4 my-4">
            <input type="password" v-model="editPwdForm.oldPassword"
                class="text-sm rounded-md border-2 w-60 md:w-44 h-8 p-2 border-b-gray-200 focus:border-blue-500 box-border outline-none"
                placeholder="Old Password" />
            <input type="password" v-model="editPwdForm.newPassword"
                class="text-sm rounded-md border-2 w-60 md:w-44 h-8 p-2 border-b-gray-200 focus:border-blue-500 box-border outline-none"
                placeholder="New Password" />
        </div>
        <div class="flex justify-center items-center gap-4 px-3">
            <el-tooltip content="点击切换图片" placement="bottom" effect="light">
                <img class="w-20 cursor-pointer" :src="`data:image/png;base64,${imageBase64}`" @click="getPicCode" />
            </el-tooltip>

            <input type="text" v-model="sendEmailForm.picCode"
                class="text-sm rounded-md border-2 w-20 h-8 p-2 border-b-gray-200 focus:border-blue-500 box-border outline-none"
                placeholder="Code" />
        </div>
        <div class="flex flex-col gap-2 justify-center items-center my-3">
            <button class="w-44 h-8 rounded-md felx justify-center items-center border border-blue-500"
                @click="sendEmailVerify">{{ sendEmailText }}</button>
            <button class="w-44 h-8 rounded-md felx justify-center items-center border border-blue-500"
                @click="editPassword">点击提交</button>
        </div>
    </div>
</template>

<style scoped></style>