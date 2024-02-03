import { defineStore } from 'pinia';
import axios from "axios"
import { useLoaddingStore } from './LoaddingStore'

export const getShopBc = defineStore("getShopBc", {
  state: () => {
    return {
      listShopBc: {},
      loaddingStore : useLoaddingStore(),
      
    };
  },
  actions: { 
    async actionGetShopBc (shopId) {
      this.loaddingStore.loading = true;
      if(shopId){
        const shopBc = await axios.post(`/api/shop/get-shop-parent?shopId=${shopId}`);
        if(shopBc.data && shopBc.data.length) {
          this.listShopBc  = shopBc.data.map(function (o) {
              return {
                value: o.shop_id,
                label: o.shop_code + "-" + o.shop_name,
              };
            })
        }
      }else{
        this.listShopBc = []
      }
      this.loaddingStore.loading = false;

    },
  },
});
