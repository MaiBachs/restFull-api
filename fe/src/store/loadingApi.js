
import { defineStore } from 'pinia';
import axios from "axios"

export const loadApi = defineStore("loadingApi", {
    state: () => {
      return {
        data: {},
        loading: false, 
      };
    },
    actions: {
      async loadingApi (formData) {
        this.loading = true; 
        const response  = await axios.post('/api/item-config/search', formData)
        if(response && response.data.data.item_config_dto_list.length) {
          console.log(response.data.data.item_config_dto_list)
          this.data =  response.data.data.item_config_dto_list;
          this.loading = false;
        }
      },
    },
  });

