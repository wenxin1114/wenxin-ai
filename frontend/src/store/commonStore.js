import { defineStore } from 'pinia'


export const useCommonStore = defineStore('common', {
    state: () => ({ 
        showAsideState: false
    }),
    actions: {
        changeShowAsideState() {
            this.showAsideState = !this.showAsideState
        }
    }
})