import { defineStore } from 'pinia';
import axios from "axios"
import { useLoaddingStore } from './LoaddingStore';
import { notify } from "@kyvg/vue3-notification";

export const getGroups = defineStore("getGroupStore", {
  state: () => {
    return {
      listGroup: {},
      listGravedad: []
    };
  },
  actions: {
    async actionGetGroup () {
      const loaddingStore = useLoaddingStore()
      try {
        loaddingStore.loadding = true;
        const response = await axios.get('/api/job/fil-group');
        this.listGroup  = response.data.data.map(function (o) {
          return {
            value: o.job_id,
            label: o.code
          };
        })
        loaddingStore.loading = false;
        
    } catch (error) {
        loaddingStore.loading = false;
        console.log(error);
        notify({
            type: "error",
            title: "Get fil group",
            text: error,
            duration: 100000,
        });
    }
  }
}
});
