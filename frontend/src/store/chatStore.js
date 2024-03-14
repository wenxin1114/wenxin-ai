import { defineStore } from 'pinia'


export const useChatStore = defineStore('chat', {
    state: () => ({
        active: null,
        chat: [],
        history: []
    }),
    getters: {
        getChatByActive: (state) => state.chat.find((e) => e.id ===  state.active),
        getHistoryByActive: (state) => state.history.find((e) => e.id === state.active)
    },
    actions: {
        addHistory(value) {
            this.chat.push({
                id: value.id,
                data: []
            })
            this.history.unshift(value)
        },
        setActive(value) {
            this.active = value
        },
        addChatData(value) {
            if (this.getChatByActive.data.length === 0) {
                this.history.forEach(item => {
                    if (item.id === this.active) {
                        item.title = value.content.substring(0, 10);
                        return;
                    }
                })
            }
            this.chat.forEach(item => {
                if (item.id === this.active) {
                    item.data.push(value)
                    return;
                }
            })
        },
        saveChatData() {
            const data = {
                active: this.active,
                chat: this.chat,
                history: this.history
            }
            localStorage.setItem("chatData", JSON.stringify(data))
        },
        importChatData(value) {
            this.active = value.active
            this.chat = value.chat
            this.history = value.history
        },
        createNewChat() {
            const data = {
                id: Date.now(),
                title: ""
            }
            this.setActive(data.id)
            this.addHistory(data)
        }
    }
})