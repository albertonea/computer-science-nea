import ky from "ky";
import {authHttpOptions} from "./httpOptions";
import { Balance } from "./balances";

export type User = {
    userId: string;
    username: string;
    createdAt: string;
}

export type Side = 'BUY' | 'SELL'

export type OrderType = 'LIMIT'

export type Trade = {
    tradeId: string;
    buy: boolean;
    price: number;
    quantity: number;
    ticker: string;
    tradeTime: string;
}

export type Order = {
   orderId: string;
   price: number;
   initialQuantity: number;
   remainingQuantity: number;
   ticker: string;
   side: Side;
   orderType: OrderType;
   orderDate: string;
}

export type Dashboard = User & {
    orders: Order[];
    balances: Balance[];
    trades: Trade[];
}

export async function getDashboard(): Promise<Dashboard> {
    return ky
        .get(`user/dashboard`, {
            ...authHttpOptions,
        }).json()
}
