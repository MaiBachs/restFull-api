<script setup>
import { ref, defineProps, defineEmits } from 'vue';
import VueFeather from "vue-feather";

const props = defineProps({
    item: {
        type: Object,
        required: true,
        default: () => ({
            title: 'Channel',
        })
    },
    path: {
        type: String,
        required: true,
        default: () => ''
    },
    
})
const isOpen = ref(false);
const handleClick = () => {
    emit('onClick', props.item.path);
    if(isOpen.value == true) {
        isOpen.value = false
    } else {
        isOpen.value = true
    }
}
const emit = defineEmits(['onClick']);
</script>

<template>
    <li :class="{
          'sidebar-main-title': item.type == 'headtitle',
        }"
    class="dropdown d-block">
        <label @click="handleClick"
          class="nav-link menu-title"
        >
          <vue-feather :type="item.icon" class="home"></vue-feather>
          <span class="menu1">
            {{ item.title }}
          </span>
           <div class="according-menu" >
            <i class="pull-right" :class="[item.active ?'fa fa-angle-right':'fa fa-angle-down' ]" ></i>
          </div>
        </label>
        <div>
            <ul v-if="path == props.item.path && isOpen">
                <li>Item 2</li>
                <li>Item 3</li>
                <li>Item 4</li>
            </ul>
        </div>
    </li>
    
</template>