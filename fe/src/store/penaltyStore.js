import { defineStore } from "pinia";
import axios from "axios";
import { useLoaddingStore } from "./LoaddingStore";
import { notify } from "@kyvg/vue3-notification";

export const usePenaltyConfigStore = defineStore("ItemConfigStore", {
  state: () => {
    return {
      quantitySuccess: {},
      fileNameError: {},
      viewFileContent: {},
      isFileError: false,
      dataSearch: {},
      loaddingStore : useLoaddingStore(),
      dataItemDetails: {}
    };
  },
  getters: {},
  actions: {
    async fetchPenaltyConfigImport(formData) {
      try {
        this.loaddingStore.loading = true;

        const response = await axios.post("/api/penalty/import", formData, {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        });
        this.quantitySuccess = response.data;
        console.log(response.data);
        this.loaddingStore.loading = false;
      } catch (error) {
        this.loaddingStore.loading = false;
        console.log(error);
        notify({
          type: "error",
          title: "Get current user",
          text: error,
          duration: 500,
        });
      }
    },
  },
});
