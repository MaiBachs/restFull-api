<template>
     <button class="btn btn-primary-light" type="button" > <a @click="logout"><i data-feather="log-out"><vue-feather type="log-out"></vue-feather></i>Log out</a></button>
</template>
<script setup>
function logout () {
             window.location.href = "/logout";

            // delete $localStorage.authenticationToken;
        }
</script>