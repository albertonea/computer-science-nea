import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export const intervalToMs = {
  FIVE_MINUTES: 5 * 60 * 1000,    // 5 minutes
  FIFTEEN_MINUTES: 15 * 60 * 1000, // 15 minutes
  ONE_HOUR: 60 * 60 * 1000,       // 1 hour
  FOUR_HOURS: 4 * 60 * 60 * 1000  // 4 hours
};

const ENVIRONMENT = window.location.href.includes('localhost')
    ? 'development' : 'production';

export const isProd = () => {
  return ENVIRONMENT === 'production';
};

export const isNotProd = () => {
  return ENVIRONMENT !== 'development';
};
