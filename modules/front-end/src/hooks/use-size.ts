import useResizeObserver from "@react-hook/resize-observer";
import {RefObject, useLayoutEffect, useState} from "react";

export const useSize = (target: RefObject<HTMLDivElement>) => {
    const [size, setSize] = useState<DOMRectReadOnly>()

    useLayoutEffect(() => {
        setSize(target.current?.getBoundingClientRect())
    }, [target])

    // Where the magic happens
    useResizeObserver(target, (entry) => setSize(entry.contentRect))
    return size
}