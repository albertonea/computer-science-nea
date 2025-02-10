import ky from "ky";
import {authHttpOptions} from "@/api/httpOptions.ts";

export type Balance = {
    ticker: string;
    balance: number;
    lockedBalance: number;
}

export function getBalances():Promise<Balance[]> {
    return ky.get(`balances`, {
        ...authHttpOptions
    }).json()
}