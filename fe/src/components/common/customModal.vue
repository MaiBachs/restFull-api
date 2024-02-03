<script setup>
import { defineProps, defineEmits, computed } from 'vue'

const props = defineProps({
    modelValue: {
        type: Boolean,
        required: true,
        default: false
    },
    title: {
      type: String,
      required: true,
    }
})
const emit = defineEmits(['update:modelValue'])
const open = computed({
    set: (value) => {
        emit('update:modelValue', value)
    },
    get: () => {
        return props.modelValue
    }
})
function close() {
    emit('update:modelValue', false)
}
</script>
<template>

<div class="modal" v-if="open" :class="{'d-block': open}">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-head btn-primary p-10">
                <div class="d-flex align-items-center">
                    <h5 class="text-white m-0 p-10">{{title}}</h5>
                    <button class="btn-close" type="button" @click="close"></button>
                </div>
                
            </div>
            <div class="modal-body">
                <slot />
            </div>
            <div class="modal-footer">
                <slot name="modalActions" :onClose="close"></slot>
                <button class="btn btn-secondary" type="button" @click="close">
                    {{ $t("CLOSE") }}
                </button>
            </div>
        </div>
    </div>
</div>





    
</template>
<style scoped>
.modal-dialog {
    max-width: 90%;
    width: 90%;
}
</style>