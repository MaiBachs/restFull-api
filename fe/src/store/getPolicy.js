import { defineStore } from 'pinia';
import axios from "axios"
export const getPolicy = defineStore("getPolicyStore", {
    state: () => {
        return {
            getPolicy: {},
            listPolicy: {},
        };
    },
    actions: { 
        async actionGetlistPolicy () {
          const loaddingStore = useLoaddingStore();
          loaddingStore.loading = true;
          const policy = await axios.get('/api/map-sale-policy/fill-list-policy');
          if(policy.data && policy.data.length) {
            this.listPolicy  = policy.data.map(function (o) {
                return {
                  value: o.id,
                  label: o.name,
                };
              })
          }
          loaddingStore.loading = false;
    
        },
    
        async actionGetPolicy () {
            const response = await axios.post('/api/map-sale-policy/search', {
                level: "3",
                parent_shop_id: "7282"
            });
            if(response.data && response.data.length) {
                // console.log(response.data)
                this.getPolicy =  response.data.map(function (o) {
                    return {
                        value: o.shop_id,
                        label: o.code + "-" + o.name,
                    };
                })
            }
        },

    },
});
