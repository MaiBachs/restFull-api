import { defineStore } from 'pinia';
import axios from "axios";
import { useLoaddingStore } from './LoaddingStore';

export const getShopChannelAudit = defineStore("getShopChannelAuditStore", {
  state: () => {
    return {
      listShopChannel: {},
    };
  },
  actions: { 
    async actionGetShopChannel () {
      const loaddingStore = useLoaddingStore();
      loaddingStore.loading = true;
      const shopChannel = await axios.get('/api/shop/shopChannel-auditor-config');
      if(shopChannel.data && shopChannel.data.length) {
        this.listShopChannel  = shopChannel.data.map(function (o) {
            return {
              value: o.shop_id,
              label: o.shop_code + "-" + o.shop_name,
            };
        });
      }
      loaddingStore.loading = false;
    },
  },
});
