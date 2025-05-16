interface ImportMetaEnv {
    readonly VITE_API_BASE_URL: string;
    readonly VITE_MOCK_ENABLED: boolean;
}
interface ImportMeta {
    readonly env: ImportMetaEnv;
}