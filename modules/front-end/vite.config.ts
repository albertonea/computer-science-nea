import { defineConfig } from 'vite'
import { TanStackRouterVite } from '@tanstack/router-plugin/vite'
import viteReact from '@vitejs/plugin-react'
import path from "path"

// https://vitejs.dev/config/
export default defineConfig({
plugins: [TanStackRouterVite(), viteReact()],
resolve: {
  alias: {
    "@": path.resolve(__dirname, "./src"),
  },
},
})

