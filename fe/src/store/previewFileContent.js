import { defineStore } from 'pinia';

export const useFileStore = defineStore('FileStore', {
    state: () => {
        return {
            currentUser: { "staff_code": "Loadding", "full_name":"Loadding", "shop_name":"Loadding" }
        }
    },
    getters: {
    },
    actions: {
        async showFileContent() {
          
        }
    },
});
