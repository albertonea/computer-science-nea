import ky from "ky";
import {httpOptions} from "@/api/httpOptions.ts";
import {User} from "@/api/user.ts";

export type AuthTokens = {
    token: string;
    refreshToken: string;
}

export type Auth = AuthTokens & {
    expiresAt: Date;
    user: User;
}


export async function login(username: string, password: string): Promise<Auth> {
    return ky
        .post(`auth/login`, {
        ...httpOptions,
        json: {
            username,
            password,
        }
        }).json()
}

export async function registerAccount(username: string, password: string) {
    await ky
        .post(`auth/register`, {
            ...httpOptions,
            json: {
                username,
                password,
            }
        })
}

export async function refreshToken(refreshToken: string): Promise<AuthTokens> {
    return ky
        .post(`auth/refresh-token?refreshToken=${refreshToken}`, {
        ...httpOptions,
        }).json()
}

export async function logout(refreshToken: string) {
    await ky.post(`auth/logout?refreshToken=${refreshToken}`, {
        ...httpOptions,
    })
}