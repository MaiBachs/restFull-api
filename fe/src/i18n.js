import { createI18n } from 'vue-i18n';
import enMessage from './lang/en.json'
import esMessage from './lang/es.json'

const messages = {
	en: { ...enMessage },
	es: { ...esMessage },
}

const i18n = createI18n({
	locale: 'es',
	messages,
	fallbackLocale:'es'
});

export default i18n;
