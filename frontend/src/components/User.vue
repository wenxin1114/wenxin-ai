<script setup>
import { computed, onMounted, h, ref } from 'vue'
import { useUserStore } from '../store/userStore';
import { ElMessage } from 'element-plus';
import { GetUserInfo, Logout } from '../api';
import LoginModel from './LoginModel.vue'
import EditPwdModel from './EditPwdModel.vue';
const userStore = useUserStore()
const loginViewState = ref(false)
const editPwdViewState = ref(false)
const user = computed(() => {
    return userStore.user
})
const closeLoginView = () => {
    loginViewState.value = false
}

const closeEditPwdView = () => {
    editPwdViewState.value = false
}


const logout = () => {
    Logout().then(res => {
        ElMessage.success(res)
        localStorage.removeItem("token")
        userStore.removeUser()
    })
}


onMounted(() => {
    const token = localStorage.getItem('token');
    if (token) {
        GetUserInfo().then(res => {
            userStore.setUser(res)
        })
    }
})
</script>

<template>
    <div class="w-64 px-2 py-4">
        <Transition name="login-transition" enter-active-class="animation-fadeIn" leave-active-class="animation-fadeOut">
            <LoginModel v-if="loginViewState" @closeLoginView="closeLoginView"></LoginModel>
        </Transition>
        <Transition name="edit-pwd-transition" enter-active-class="animation-fadeIn" leave-active-class="animation-fadeOut">
            <EditPwdModel v-if="editPwdViewState" @closeEditPwdView="closeEditPwdView"></EditPwdModel>
        </Transition>
        


        <div class="flex gap-4">
            <div class="w-16 h-16">
                <img src="../assets/user.jpg" alt="">
            </div>
            <div v-if="Object.keys(user).length > 0" class="flex flex-col gap-1">
                <div class="font-black">
                    <span>{{ user.username }}</span>
                </div>
                <div class="text-xs">
                    <span>ID: </span>
                    <span>{{ user.userId }}</span>
                </div>
                <div class="text-xs">
                    <span>Token: </span>
                    <span>{{ user.token }}</span>
                </div>
            </div>
            <div v-else class="flex justify-center items-center gap-1">
                <div class="font-black">
                    <span>未登录</span>
                </div>
            </div>
        </div>

        <div v-if="Object.keys(user).length > 0" class="flex text-black flex-wrap justify-between my-3">
            <!-- <button class="w-[118px] my-1 h-9 border-2 hover:bg-slate-100">个人资料</button>
            <button class="w-[118px] my-1 h-9 border-2 hover:bg-slate-100">今日签到</button> -->
            <button class="w-[118px] my-1 h-9 border-2 hover:bg-slate-100" @click="editPwdViewState = true">修改密码</button>
            <button class="w-[118px] my-1 h-9 border-2 hover:bg-slate-100" @click="logout">退出登录</button>
        </div>
        <div v-else class="flex text-black my-3">
            <button class="w-full my-1 h-9 border-2 hover:bg-slate-100" @click="loginViewState = true">用户登录</button>
        </div>
    </div>
</template>

<style scoped></style>