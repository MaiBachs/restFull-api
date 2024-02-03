<script setup>
import { computed, defineProps } from 'vue';
const props = defineProps({
  modalId: {
    type: String,
    required: true,
  },
  title: {
    type: String,
    required: true,
  },
});

const modalLabel = computed(() => `${props.modalId}Label`);
</script>
<template>
  <div
    :id="modalId"
    class="modal fade"
    tabindex="-1"
    role="dialog"
    :aria-labelledby="modalLabel"
    aria-hidden="true"
    data-backdrop="static"
  >
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 :id="modalLabel" class="modal-title">{{ title }}</h5>
          <button
            class="btn-close"
            type="button"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
            <slot />
        </div>
        
        <div class="modal-footer">
          <slot name="modalActions" :onClose="close"></slot>
          <button class="btn btn-primary" ref="closeElement" type="button" data-bs-dismiss="modal">
            {{ $t("CLOSE") }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
