import { defineStore } from 'pinia';
import axios from "axios"

export const getlistLog = defineStore("getlistLogStore", {
    state: () => {
        return {
            listgetLog: [],
        };
    },
    actions: {
        async actionGetlistLog() {
            try {
                const getlistLog = await axios.get('/api/log/view-log-channel-checklist');
                if (getlistLog.data.data && getlistLog.data.data.length) {
                    this.listgetLog = getlistLog.data.data; 
                }
                console.log(this.listgetLog);
            } catch (error) {
                // 
                console.error(error);
            }
        },
    },
});