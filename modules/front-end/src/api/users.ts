import {Balance} from "@/api/balances.ts";
import ky from "ky";
import {httpOptions} from "@/api/httpOptions.ts";

type User = {
    userId: string;
    username: string;
    createdAt: Date;
    balances: Balance[];
}

export async function loginRequest(username: string): Promise<User> {
    return ky
        .get(`users/login/${username}`, {
        ...httpOptions
        }).json()
}