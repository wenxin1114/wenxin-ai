<script setup>
import Aside from './components/Aside.vue'
import Main from './components/Main.vue'
</script>

<template>
  <div class="relative h-screen p-0 md:p-4">
    <div class="flex border-gray-300 h-full rounded-lg shadow-lg">
      <div class="app-aside">
          <Aside></Aside>
      </div>
      <div class="app-main border flex-1">
        <Main></Main>
      </div>
    </div>
  </div>
</template>
