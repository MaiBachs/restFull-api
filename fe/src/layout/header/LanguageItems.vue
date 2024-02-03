<template>
    <div id="app">
        <select v-model="$i18n.locale"  @change="$i18n.locale = $i18n.locale">
            <option value="en">English</option>
            <option value="es">Spanish</option>
        </select>
    </div>
</template>

<script setup>

</script>

<style scoped>


.language.notification-dropdown  {
    width: 180px !important;
}
</style>