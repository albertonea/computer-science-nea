import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

const ENVIRONMENT = window.location.href.includes('localhost')
    ? 'development' : 'production';

export const isProd = () => {
  return ENVIRONMENT === 'production';
};

export const isNotProd = () => {
  return ENVIRONMENT !== 'development';
};
