<script setup>
import { computed, ref, onMounted, watch, nextTick } from 'vue'
import ChatMessage from './ChatMessage.vue'
import { SendMessage } from '../api'
import { useChatStore } from '../store/chatStore';
import { useUserStore } from '../store/userStore';
import { ElMessage } from 'element-plus';
import { useCommonStore } from '../store/commonStore';


const chatStore = useChatStore()
const userStore = useUserStore()
const question = ref("")
const commonStore = useCommonStore()
const chatScrollRef = ref(null)
const changeShowAsideState = () => {
    commonStore.changeShowAsideState()
}
const user = computed(() => {
    return userStore.user
})
const showAsideState = computed(() => {
    return commonStore.showAsideState
})
const history = computed(() => {
    return chatStore.getHistoryByActive
})
const chat = computed(() => {
    return chatStore.getChatByActive
})
const formattedDate = (timestamp) => {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');

    const formattedDate = `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
    return formattedDate;
}
const sendMessage = () => {
    if (Object.keys(user.value).length == 0) {
        ElMessage.warning("用户未登录")
        return;
    }
    if (question.value.length === 0) {
        ElMessage.warning("你要发送的内容不能为空")
        return;
    }
    if (chatStore.history.length === 0) {
        chatStore.createNewChat()
    }
    const data = {
        question: question.value,
        id: chatStore.active
    }
    chatStore.addChatData({
        createTime: formattedDate(Date.now()),
        content: question.value,
        inversion: 1
    })
    SendMessage(data).then(res => {
        console.log(res);
        chatStore.addChatData({
            createTime: formattedDate(Date.now()),
            content: res.reply,
            inversion: 0
        })
    })
    question.value = ""
}
const handleKeyDown = (event) => {
    console.log(history)
    // 检测是否同时按下了回车键和Shift键
    if (event.key === 'Enter' && !event.shiftKey) {
        event.preventDefault(); // 阻止默认的换行行为
        sendMessage();
    }

}
const beforeunloadHandler = (e) => {
    // 保存聊天数据
    chatStore.saveChatData()
}

const scrollToBottom = () => {
    nextTick(() => {
        if (chatScrollRef.value) {
            chatScrollRef.value.scrollTop = chatScrollRef.value.scrollHeight;
        }
    });
}


onMounted(() => {
    // 监听浏览器关闭
    window.addEventListener('beforeunload', e => beforeunloadHandler(e))
    // 加载本地聊天数据
    const chatData = JSON.parse(localStorage.getItem('chatData'))
    if (chatData && Object.keys(chatData).length > 0) {
        // 有数据导入chatStore
        chatStore.importChatData(chatData)
    }
})

watch(() => chat.value, () => {
    scrollToBottom()
}, { deep: true })

</script>

<template>
    <div class="flex flex-col w-full h-full">
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
        <!-- chat box -->
        <div class="flex-1 overflow-hidden">
            <div class="h-full overflow-hidden overflow-y-auto" ref="chatScrollRef">
                <div id="image-wrapper" class="w-full max-w-screen-xl m-auto dark:bg-[#101014] p-4">
                    <ChatMessage v-if="chat" v-for="item in chat.data" :data="item"></ChatMessage>
                </div>
            </div>
        </div>
        <!-- input box -->
        <div class="h-14 flex p-3" :class="{ 'hidden': showAsideState }">
            <textarea v-model="question" @keydown="handleKeyDown"
                class="scrollbar-hidden p-1 text-sm border flex-1 resize-none h-auto hover:border-green-500 focus:border-green-500 focus:outline-none"
                rows="1" placeholder="Ask me anything...(Shift + Enter = line break)"></textarea>
            <button @click="sendMessage" class="border text-white bg-green-500 py-1 px-4">发送</button>
        </div>
    </div>
</template>

<style scoped>
.scrollbar-hidden {
    -ms-overflow-style: none;
    /* IE and Edge */
    scrollbar-width: none;
    /* Firefox */
}

.scrollbar-hidden::-webkit-scrollbar {
    display: none;
    /* Chrome, Safari */
}

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