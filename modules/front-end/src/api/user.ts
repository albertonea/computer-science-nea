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

type DashboardTrade = {
    tradeId: string;
    buy: boolean;
    price: number;
    quantity: number;
    ticker: string;
    tradeTime: string;
}

export type DashboardOrder = {
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
    orders: DashboardOrder[];
    balances: Balance[];
    trades: DashboardTrade[];
}

export async function getDashboard(): Promise<Dashboard> {
    return ky
        .get(`user/dashboard`, {
            ...authHttpOptions,
        }).json()
}
