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
  },
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
    <div class="modal justify-content-center align-items-center" v-if="open" :class="{'d-flex': open}">
        <div class="modal-backdrop fade show z-2"></div>
        <div class="modal-content bg-white z-3 w-25">
            <div class="modal-header bg-danger d-block">
                <i class="icofont icofont-fire-alt"></i>
                {{ title }}
            </div>
            <div class="modal-body">
                <slot />
            </div>
            <div class="modal-footer">
              <slot name="modalActionsError"></slot>
              <button class="btn btn-secondary" @click="close" type="button">
                {{ $t("CLOSE") }}
              </button>
            </div>
        </div>
    </div>
</template>