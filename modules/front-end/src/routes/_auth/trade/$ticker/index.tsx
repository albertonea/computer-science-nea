import { createFileRoute } from '@tanstack/react-router'
import { useMemo } from 'react'
import { Responsive, WidthProvider } from 'react-grid-layout'
import InterfaceChart from '@/components/interface-ui/InterfaceChart.tsx'
import InterfaceNav from '@/components/interface-ui/InterfaceNav.tsx'
import InterfaceOpenOrders from '@/components/interface-ui/InterfaceOpenOrders.tsx'
import InterfaceTrading from '@/components/interface-ui/InterfaceTrading.tsx'
import InterfaceTrades from "@/components/interface-ui/InterfaceTrades.tsx";
import {TradingProvider} from "@/context/trading-provider.tsx";
import {useQuery} from "@tanstack/react-query";
import {getCandlesticks} from "@/api/candlestick.ts";
import {getOpenOrders} from "@/api/orders.ts";
import {map} from "ramda";

export const Route = createFileRoute('/_auth/trade/$ticker/')({
  component: Interface,
})

function Interface() {
  const {ticker} = Route.useParams()
  const ResponsiveGridLayout = useMemo(() => WidthProvider(Responsive), [])



  const {data:openOrders} = useQuery({
    queryKey: ['openOrders', ticker],
    queryFn: () => getOpenOrders(ticker),
    select: data => map((d) => {
      return {
        createdAt: d.createdAt,
        ticker: d.ticker,
        orderId: d.orderId,
        price: d.price,
        initialQuantity: d.initialQuantity,
        remainingQuantity: d.remainingQuantity,
        side: d.side
      }
    },data),
    staleTime: 0
  })

  if (openOrders) return (
      <TradingProvider orders={openOrders} ticker={ticker}>
        <ResponsiveGridLayout
            className="layout overflow-hidden bg-muted"
            breakpoints={{xxs: 0}}
            cols={{xxs: 24}}
            margin={[2, 2]}
            rowHeight={75}
        >
          <div
              className="bg-card"
              key="a"
              style={{zIndex: 10}}
              data-grid={{x: 0, y: 0, w: 15, h: 1, static: true}}
          >
            <InterfaceNav/>
          </div>
          <div
              className="bg-card"
              key="b"
              style={{zIndex: 10}}
              data-grid={{x: 0, y: 3, w: 15, h: 6, isDraggable: false}}
          >
            <InterfaceChart/>
          </div>
          <div
              className="bg-card overflow-scroll scrollbar-hide"
              key="c"
              style={{zIndex: 10}}
              data-grid={{x: 0, y: 20, w: 15, h: 5, isDraggable: false}}
          >
            <InterfaceOpenOrders/>
          </div>
          <div
              className="bg-card overflow-scroll scrollbar-hide"
              key="e"
              style={{zIndex: 10}}
              data-grid={{x: 15, y: 14, w: 9, h: 7}}
          >
            <InterfaceTrades/>
          </div>
          <div
              className="bg-card"
              key="f"
              style={{zIndex: 10}}
              data-grid={{x: 15, y: 2, w: 9, h: 5}}
          >
            <InterfaceTrading/>
          </div>
        </ResponsiveGridLayout>
      </TradingProvider>
  )
  return null
}
