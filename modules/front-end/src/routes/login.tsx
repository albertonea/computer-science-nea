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
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "@/components/ui/card.tsx";

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

    const onFormSubmit = async (evt: React.FormEvent<HTMLFormElement>) => {
        setIsSubmitting(true)
        try {
            evt.preventDefault()
            const data = new FormData(evt.currentTarget)
            const fieldValue = data.get('username')

            if (!fieldValue) return
            const username = fieldValue.toString()
            await auth.login(username)

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
                    <Card className="w-full max-w-sm">
                        <CardHeader>
                            <CardTitle className="text-2xl">Login</CardTitle>
                            <CardDescription>
                                Enter a username below to start trading.
                            </CardDescription>
                        </CardHeader>
                        <CardContent className="grid gap-4">
                            <div className="grid gap-2">
                                <Label htmlFor="username-input">Username</Label>
                                <Input id="username-input" name="username" type="text" placeholder="Enter your name" required/>
                            </div>
                        </CardContent>
                        <CardFooter>
                            <Button variant="default" type="submit" className="w-full">
                                {isLoggingIn ? 'Loading...' : 'Start'}
                            </Button>
                        </CardFooter>
                    </Card>
                </fieldset>
            </form>
        </div>
)
}
