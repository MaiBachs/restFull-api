import axios from "axios";
import { ref } from "vue";
import { notify } from "@kyvg/vue3-notification";
import { useLoaddingStore } from "@/store/LoaddingStore";
import { useUserStore } from '@/store/UserStore';
import { i18n } from "@/main.js";

export default function useBTS() {
    const loaddingStore = useLoaddingStore();
    const userStore = useUserStore()
    const listStaff = ref([])
    const listBC = ref([])
    const listNewItem = 
        {
          value: "",
          label: "---" + i18n.global.t('STATUS.ALL') + "---",
          code: "---" + i18n.global.t('STATUS.ALL') + "---"
        }
    ;    
    async function getBC(shop_id) {
        listStaff.value = []
        if (userStore.currentUser.shop_type == "BR" && userStore.currentUser.branch) {
          const loaddingStore = useLoaddingStore()
          loaddingStore.loading = true;
          const response = await axios.post('/api/shop/shop-tree', {
              level: "4",
              parent_shop_id: shop_id
          });
          if(response.data && response.data.length) {
              listBC.value =  response.data.map(function (o) {
                  return {
                      value: o.shop_id,
                      label: o.code + "-" + o.name,
                      code: o.code
                  };
              })
              listBC.value.unshift(listNewItem);
              console.log(listBC.value)
          }
          loaddingStore.loading = false;
      }
      if (userStore.currentUser.shop_type == "BC" && userStore.currentUser.branch && userStore.currentUser.bc) {
          const loaddingStore = useLoaddingStore()
          listBC.value = [userStore.currentUser.bc].map(function (o) {
              return {
                  value: o.shop_id,
                  label: o.code + "-" + o.name,
                  code: o.code
              };
          })
          
          getStaff(shop_id)
          loaddingStore.loading = false;

      }

      if (userStore.currentUser.shop_type == "AGENT") {
          // if (vm.userProfile.branch) {
          //     vm.listSucursals = [vm.userProfile.branch];
          //     vm.allBranchs = [vm.userProfile.branch];
          //     vm.sucursalChannel = vm.listSucursals[0];
          // }
          // if (vm.userProfile.bc) {
          //     vm.centrodenegocios = [vm.userProfile.bc];
          //     vm.centrodenegocioChannel = vm.centrodenegocios[0];
          // } else {
          //     if (vm.centrodenegocioChannel) {
          //         $scope.getZonalAgent(vm.centrodenegocioChannel);
          //     } else if (vm.sucursalChannel) {
          //         $scope.getZonalAgent(vm.sucursalChannel);
          //     }

          // }


      }
        // try {
        //   loaddingStore.loading = true;
        //   const response = await axios.post('/api/shop/shop-tree', {
        //     level: "4", 
        //     parent_shop_id:shop_id
        //   });
        //   if(response.data && response.data && response.data.length) {
        //     listBC.value = response.data.map(function(o) {
        //       return {
        //         value: o.shop_id,
        //         label: o.code + '-' + o.name,
        //         code: o.code
        //       }
        //     })
        //   }
        //   loaddingStore.loading = false;
        //   console.log(listBC.value)
        // } catch (error) {
        //   loaddingStore.loading = false;
        //   notify({
        //     type: "error",
        //     title: "Get current user",
        //     text: error,
        //     duration: 500,
        //   });
        // }
    }
    async function getStaff(shop_id) {
        listStaff.value = []
        try {
          loaddingStore.loading = true;
          const response = await axios.post('/api/staff/search', {
              shop_id: shop_id || ''
          });
          console.log(response)
          if(response.data && response.data.data && response.data.data.length) {
            listStaff.value = response.data.data.map(function(o) {
              return {
                value: o.id,
                label: o.code + '-' + o.name
              }
            })
            listStaff.value.unshift(listNewItem);
          }
          loaddingStore.loading = false;
        } catch (error) {
          loaddingStore.loading = false;
          notify({
            type: "error",
            title: "Get current user",
            text: error,
            duration: 500,
          });
        }
    }

    return {
        getBC,
        getStaff,
        listBC,
        listStaff
    }
}