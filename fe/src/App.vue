<template>
    <router-view />
    <div class="loader-wrapper" v-if='loaddingStore.loading'>
        <div class="theme-loader">
            <div class="loader-p"></div>
        </div>
    </div>
    <notifications/>
</template>
<script setup>
// import { useUserStore } from './store/UserStore';
import { useLoaddingStore } from './store/LoaddingStore';
const loaddingStore = useLoaddingStore()
// const userStore = useUserStore()
// userStore.fetchCurrentUser()
</script>
