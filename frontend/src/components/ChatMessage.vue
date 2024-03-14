<script setup>
import { ref, computed, onMounted } from 'vue'
import userJpg from '../assets/user.jpg'
import assistantJpg from '../assets/assistant.jpg'
const props = defineProps(['data'])
const previewRef = ref(null)
const imageUrl = computed(() => {
    return props.data.inversion === 1 ? userJpg : assistantJpg
})
onMounted(() => {

})
</script>

<template>
    <div v-if="data">
        <!-- user chat message -->
        <div class="flex gap-2 mb-5" :class="{ 'flex-row-reverse': data.inversion === 1 }">
            <!-- head portrait -->
            <div class="w-10 h-10 flex-shrink-0"> <!-- 添加h-10和flex-shrink-0 -->
                <img :src="imageUrl" alt="" class="w-full h-full object-cover rounded-full"> <!-- 确保图片填满容器且圆角显示 -->
            </div>
            <!-- chat info -->
            <div class="flex flex-col gap-2 flex-grow"
                :class="{ 'items-end': data.inversion, 'items-start': data.inversion !== 1 }"> <!-- 添加flex-grow -->
                <!-- chat time -->
                <div class="text-xs">{{ data.createTime }}</div>
                <!-- chat content -->
                <v-md-preview ref="previewRef" :text="data.content" class="bg-green-200 p-2 rounded-md text-wrap flex-grow ">
                </v-md-preview >
            </div>
        </div>
    </div>
</template>

<style scoped>
.github-markdown-body {
  padding: 0 !important;
  z-index: -10 !important;
}

</style>