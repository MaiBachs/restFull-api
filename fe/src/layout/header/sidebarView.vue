<template>
 
    <div class="logo-wrapper">
      <Logo />

    </div>
    <div class="dark-logo-wrapper"><router-link to="/"><img class="img-fluid" src="..\..\assets\images\logo\bitel-logo1.png" alt=""></router-link></div>
     <!-- <div class="toggle-sidebar" @click="toggle_sidebar">
        <vue-feather
          class="status_toggle middle"
          type="align-center"
          id="sidebar-toggle"
        ></vue-feather>
      </div> -->
   
 
</template>
<script setup>
  // import { mapState } from 'vuex';
   import Logo from '../slidebar/logoView.vue';
</script>
