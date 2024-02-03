import { defineStore } from 'pinia';
import axios from "axios"
export const getlistPolicy = defineStore("getlistPolicyStore", {
    state: () => {
        return {
            listgetPolicy: [],
        };
    },
    actions: {
        async actionGetlistPolicy() {
            const GetlistPolicy = await axios.get('/api/map-sale-policy/fill-list-policy');
            if (GetlistPolicy.data.data && GetlistPolicy.data.data.length) {
                this.listgetPolicy = GetlistPolicy.data.data.map(function (o) {
                    return {
                        value: o.id,
                        label: o.name,
                    };
                })
            }
            console.log(this.listgetPolicy)
        },
    },
});

