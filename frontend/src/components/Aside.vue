<script setup>
import { computed, onMounted } from 'vue'
import User from './User.vue'
import { useChatStore } from '../store/chatStore';
import { useCommonStore } from '../store/commonStore';
import { useUserStore } from '../store/userStore';
import { ElMessage } from 'element-plus';
const commonStore = useCommonStore()
const chatStore = useChatStore()
const userStore = useUserStore()
const user = computed(() => {
    return userStore.user
})
const showAsideState = computed(() => {
    return commonStore.showAsideState
})
const active = computed(() => {
    return chatStore.active
})

const history = computed(() => {
    return chatStore.history
})
const changeShowAsideState = () => {
    commonStore.changeShowAsideState()
}
const createNewChat = () => {
    if (Object.keys(user.value).length == 0) {
        ElMessage.warning("用户未登录")
        return;
    }
    chatStore.createNewChat()

}
const changeActive = (id) => {
    chatStore.setActive(id)
}

</script>

<template>
    <div :class="{ 'fixed z-10': showAsideState, 'hidden': !showAsideState }" class="md:block bg-white">
        <!-- md show  -->
        <div class="md:hidden">
            <div class="flex border-b p-2 gap-2 fixed">
                <div @click="changeShowAsideState" class="cursor-pointer"><svg width="24" height="24"
                        viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M7.94971 11.9497H39.9497" stroke="#333" stroke-width="4" stroke-linecap="round"
                            stroke-linejoin="round" />
                        <path d="M7.94971 23.9497H39.9497" stroke="#333" stroke-width="4" stroke-linecap="round"
                            stroke-linejoin="round" />
                        <path d="M7.94971 35.9497H39.9497" stroke="#333" stroke-width="4" stroke-linecap="round"
                            stroke-linejoin="round" />
                    </svg></div>
                <div v-if="history">{{ history.title }}</div>
                <div v-else="history">New Chat</div>
            </div>
        </div>
        <div class="w-72 border border-b-0 flex flex-col justify-center items-center">
            <!-- create new chat button -->
            <button @click="createNewChat"
                class="w-60 border border-dotted h-9 flex items-center justify-center text-center mt-12 md:my-5 rounded hover:border-green-500 hover:text-green-500">New
                Chat</button>
            <!-- history list -->
            <div
                class="w-64 h-96 p-2 border-b-0 flex flex-col gap-2 overflow-hidden hover:overflow-y-auto hover:overflow-x-hidden">
                <div v-for="item in history" @click="changeActive(item.id)">
                    <a :class="{ 'bg-neutral-100 border-green-500': active === item.id }"
                        class="w-60 border h-10 flex items-center text-sm p-3 gap-3 rounded-md relative box-border cursor-pointer break-all hover:border-green-500 hover:bg-neutral-100">
                        <span><svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
                                aria-hidden="true" role="img" class=" iconify iconify--ri" width="1em" height="1em"
                                viewBox="0 0 24 24">
                                <path fill="currentColor"
                                    d="M2 8.994A5.99 5.99 0 0 1 8 3h8c3.313 0 6 2.695 6 5.994V21H8c-3.313 0-6-2.695-6-5.994zM20 19V8.994A4.004 4.004 0 0 0 16 5H8a3.99 3.99 0 0 0-4 3.994v6.012A4.004 4.004 0 0 0 8 19zm-6-8h2v2h-2zm-6 0h2v2H8z">
                                </path>
                            </svg></span>
                        <div class="relative flex-1 overflow-hidden break-all text-ellipsis whitespace-nowrap">
                            <span>{{ item.title || "New Chat" }}</span>
                        </div>
                    </a>
                </div>
            </div>
            <!-- user -->
            <User></User>
        </div>

    </div>
</template>

<style scoped>
::-webkit-scrollbar {
    width: 6px;
    box-sizing: border-box;
}

::-webkit-scrollbar-thumb {
    background-color: rgb(156, 155, 155);
    border-radius: 10px;
    /* 给滑块添加圆角 */
}

/* 自定义滚动条轨道 */
::-webkit-scrollbar-track {
    border-radius: 10px;
    /* 给轨道添加圆角 */
}
</style>