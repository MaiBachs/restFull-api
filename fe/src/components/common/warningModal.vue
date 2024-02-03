<script setup>
import { computed, defineEmits, defineProps, onMounted, ref } from 'vue';
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

const emit = defineEmits(['closeModal'])
const modalLabel = computed(() => `${props.modalId}Label`);
const closeElement = ref(null)
const myModalEl = ref(null)

function close() {
  if (closeElement.value) {
    closeElement.value.click()
  }
}

onMounted(() => {
  myModalEl.value.addEventListener('hide.bs.modal', event => {
    emit('closeModal', event)
  })
})

</script>
<template>
  <div
    class="modal fade"
    tabindex="-1"
    aria-hidden="true"
    :id="modalId"
    ref="myModalEl"
    role="dialog"
    :aria-labelledby="modalLabel"
    data-backdrop="static"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header bg-warning">
          <h5 class="modal-title" :id="modalLabel">
            {{ title }}
          </h5>
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
          <button class="btn btn-secondary" ref="closeElement" type="button" data-bs-dismiss="modal">
            {{ $t("CLOSE") }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
