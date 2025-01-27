import {
    createFileRoute,
    redirect,
    useRouter,
    useRouterState,
} from '@tanstack/react-router'
import { z } from 'zod'

import { useAuth } from '../auth'
import {useState} from "react";
import {Label} from "@/components/ui/label.tsx";
import {Input} from "@/components/ui/input.tsx";
import {Button} from "@/components/ui/button.tsx";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card.tsx";

// eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
const fallback = '/dashboard' as const

export const Route = createFileRoute('/login')({
    validateSearch: z.object({
        redirect: z.string().optional().catch(''),
    }),
    beforeLoad: ({ context, search }) => {
        if (context.auth?.isAuthenticated) {
            throw redirect({ to: search.redirect || fallback })
        }
    },
    component: LoginComponent,
})

function LoginComponent() {
    const auth = useAuth()
    const router = useRouter()
    const isLoading = useRouterState({ select: (s) => s.isLoading })
    const navigate = Route.useNavigate()
    const [isSubmitting, setIsSubmitting] = useState(false)

    const onFormSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        setIsSubmitting(true)
        try {
            e.preventDefault()
            const data = new FormData(e.currentTarget)
            const usernameFieldValue = data.get('username')
            const passwordFieldValue = data.get('password')

            if (!usernameFieldValue || !passwordFieldValue) return
            const username = usernameFieldValue.toString()
            const password = passwordFieldValue.toString()
            await auth.loginAndSaveContents(username, password)

            await router.invalidate()

            // This is just a hack being used to wait for the auth state to update
            // in a real app, you'd want to use a more robust solution

            await navigate({ to: '/dashboard' })
        } catch (error) {
            console.error('Error logging in: ', error)
        } finally {
            setIsSubmitting(false)
        }
    }

    const isLoggingIn = isLoading || isSubmitting

    return (
        <div className="flex w-full h-full justify-center items-center">
            <form onSubmit={onFormSubmit}>
                <fieldset disabled={isLoggingIn} className="w-full grid gap-2">
                    <Card>
                        <CardHeader>
                            <CardTitle className="text-2xl">Login</CardTitle>
                            <CardDescription>
                                Enter your username below to login to your account
                            </CardDescription>
                        </CardHeader>
                        <CardContent>
                            <div className="flex flex-col gap-6">
                                <div className="grid gap-2">
                                    <Label htmlFor="username">Username</Label>
                                    <Input
                                        id="username"
                                        name="username"
                                        type="username"
                                        placeholder="trader123"
                                        required
                                    />
                                </div>
                                <div className="grid gap-2">
                                    <div className="flex items-center">
                                        <Label htmlFor="password">Password</Label>
                                    </div>
                                    <Input id="password" name="password" type="password" required />
                                </div>
                                <Button type="submit" className="w-full">
                                    Login
                                </Button>
                            </div>
                            <div className="mt-4 text-center text-sm">
                                Don&apos;t have an account?{" "}
                                <a href="/register" className="underline underline-offset-4">
                                    Sign up
                                </a>
                            </div>
                        </CardContent>
                    </Card>
                </fieldset>
            </form>
        </div>
)
}
