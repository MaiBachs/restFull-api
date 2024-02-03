import {ref} from 'vue';
import { defineStore } from 'pinia';
import axios from "axios"

export const getGravedad = defineStore("getGravedadStore", () => {
    const getListGravedad = ref([]);
    const getListReason = ref([]);
    const listAddGravedad = ref([])
    const listAddReason = ref([])
  
    const actionGetListGravedad = async () => {
      const { data: gravedad } =  await axios.get("/api/item-config/gravedad");
      listAddGravedad.value = gravedad.data
       getListGravedad.value  = gravedad.data.map(function (o) {
        return {
          value: o.code,
          label: o.value,
        };
      })
      const { data: reason } =  await axios.get("/api/item-config/reason");
      listAddReason.value = reason.data
        getListReason.value  = reason.data.map(function (o) {
          return {
            value: o.reason_id,
            label: o.name,
          };
        })
    };  
    return {
      getListGravedad,
      actionGetListGravedad,
      getListReason,
      listAddGravedad,
      listAddReason
    };
  });


  
