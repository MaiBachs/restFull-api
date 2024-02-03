import { defineStore } from 'pinia';
import axios from "axios"

export const getUser = defineStore("getlistUser", {
    state: () => {
        return {
            listgetUser: [],  // Fix: Correct the state property name
        };
    },
    actions: {
        async actionGetlistUser() {
            try {
                const GetlistUser = await axios.get('/api/penalty/fil-user-type');
                if (GetlistUser.data.data && GetlistUser.data.data.length) {
                    this.listgetUser = GetlistUser.data.data.map(function (o) {
                        return {
                            value: o.pos_id,
                            label: o.pos_name,
                        };
                    });
                }
                console.log(this.listgetUser);
            } catch (error) {
                console.error("Error fetching user list:", error);
            }
        },
    },
});
