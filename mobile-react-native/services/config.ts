import Constants from 'expo-constants';

const FALLBACK_API_BASE_URL = 'http://192.168.1.100:8080';

type ExpoExtra = {
  apiBaseUrl?: string;
};

const expoExtra = (Constants.expoConfig?.extra ?? {}) as ExpoExtra;

export const API_BASE_URL = expoExtra.apiBaseUrl ?? FALLBACK_API_BASE_URL;
