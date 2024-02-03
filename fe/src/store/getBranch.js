import { defineStore } from 'pinia';
import axios from "axios";
import { useLoaddingStore } from './LoaddingStore';

export const getBranch = defineStore("getBranchStore", {
  state: () => {
    return {
      getBranch: {},
    };
  },
  actions: {
    async actionGetBranch () {
      const loaddingStore = useLoaddingStore();
      loaddingStore.loading = true;
      const response = await axios.post('/api/shop/shop-tree', {
        level: "3", 
        parent_shop_id: "7282"
      });
      if(response.data && response.data.length) {
        // console.log(response.data)
        this.getBranch =  response.data.map(function (o) {
            return {
              value: o.shop_id,
              label: o.code + "-" + o.name,
              code: o.code
            };
          })
      }
      loaddingStore.loading = false;
    },
   
  },
});