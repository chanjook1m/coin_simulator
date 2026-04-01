// src/TradingScreen.jsx
import React, { useEffect, useState } from "react";

const API_BASE_URL =
  import.meta?.env?.VITE_API_BASE_URL ||
  "http://ec2-3-27-221-152.ap-southeast-2.compute.amazonaws.com:8081/api/v1";

function TradingScreen() {
  // 공통 상태
  const [symbols, setSymbols] = useState([]); // 코인 목록
  const [symbolsLoading, setSymbolsLoading] = useState(false);
  const [symbolsError, setSymbolsError] = useState(null);
  const [selectedSymbol, setSelectedSymbol] = useState(null);

  const [priceLoading, setPriceLoading] = useState(false);
  const [priceError, setPriceError] = useState(null);
  const [latestPrice, setLatestPrice] = useState(null);

  const [userId, setUserId] = useState("1");
  const [quantity, setQuantity] = useState("0.01");
  const [orderLoading, setOrderLoading] = useState(false);
  const [orderStatus, setOrderStatus] = useState(null);
  const [orderError, setOrderError] = useState(null);
  const [orderResponse, setOrderResponse] = useState(null);

  const [rightTab, setRightTab] = useState("order"); // order | wallet | ledger

  const [equity, setEquity] = useState(10000.0);
  const [floatingPnl, setFloatingPnl] = useState(123.45);
  const [inUse, setInUse] = useState(9080.82);

  const currentSymbol = selectedSymbol ||
    symbols[0] || { symbol: "-", price: 0, change24h: 0 };

  // ----- 코인 목록 가져오기 (버튼 클릭 시 실행) -----
  const fetchSymbols = async () => {
    setSymbolsLoading(true);
    setSymbolsError(null);
    try {
      const res = await fetch(`${API_BASE_URL}/coins`);
      const data = await res.json();
      const mapped =
        data?.map((c) => ({
          symbol: c.symbol || c.ticker || "UNKNOWN",
          name: c.name || c.symbol || "Unknown",
          price: c.price || c.currentPrice || 0,
          change24h: c.change24h || c.changeRate || 0,
        })) || [];
      setSymbols(mapped);
      if (!selectedSymbol && mapped.length > 0) {
        setSelectedSymbol(mapped[0]);
      }
    } catch (e) {
      console.error(e);
      setSymbolsError(e.message || "Failed to load symbols");
    } finally {
      setSymbolsLoading(false);
    }
  };

  // ----- 가격 보기 -----
  const fetchPrice = async () => {
    if (!selectedSymbol) return;
    setPriceLoading(true);
    setPriceError(null);
    setLatestPrice(null);

    try {
      const res = await fetch(
        `${API_BASE_URL}/prices/${encodeURIComponent(selectedSymbol.symbol)}`,
      );
      const data = await res.json();
      const p = data.price ?? data.currentPrice ?? null;
      if (p == null) {
        throw new Error("Price not found in response");
      }
      setLatestPrice(p);
    } catch (e) {
      console.error(e);
      setPriceError(e.message || "Failed to fetch price");
    } finally {
      setPriceLoading(false);
    }
  };

  // ----- 시장가 매수 -----
  const buyMarket = async () => {
    if (!selectedSymbol) return;

    setOrderError(null);
    setOrderStatus(null);
    setOrderResponse(null);
    setOrderLoading(true);

    try {
      const res = await fetch(`${API_BASE_URL}/orders/market/buy`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-USER-ID": String(userId),
        },
        body: JSON.stringify({
          symbol: selectedSymbol.symbol,
          quantity: Number(quantity),
        }),
      });

      let data = null;
      try {
        data = await res.json();
      } catch (e) {
        console.warn("Failed to parse JSON:", e);
      }

      if (!res.ok) {
        throw new Error(
          (data && (data.code || data.message)) ||
            `Error: ${res.status} ${res.statusText}`,
        );
      }

      setOrderResponse(data);
      setOrderStatus("✅ 시장가 매수 성공");
    } catch (e) {
      console.error(e);
      setOrderError(e.message || "Unknown error");
      setOrderStatus("❌ 시장가 매수 실패");
    } finally {
      setOrderLoading(false);
    }
  };

  return (
    <div className="h-screen w-full bg-[#050816] text-gray-100 flex flex-col">
      {/* 헤더 */}
      <header className="h-12 border-b border-gray-800 flex items-center justify-between px-4 text-xs">
        <div className="flex items-center gap-3">
          <span className="text-sm font-semibold text-white">
            COIN SIMULATOR
          </span>
          <span className="text-[11px] text-gray-400">
            {currentSymbol.symbol}
          </span>
        </div>
        <div className="flex items-center gap-4 text-[11px] text-gray-400">
          <span>Equity: {equity.toLocaleString()} USDT</span>
          <span>
            PnL:{" "}
            <span
              className={floatingPnl >= 0 ? "text-emerald-400" : "text-red-400"}
            >
              {floatingPnl >= 0 ? "+" : ""}
              {floatingPnl.toFixed(2)} USDT
            </span>
          </span>
        </div>
      </header>

      {/* LEFT / CENTER / RIGHT */}
      <div className="flex flex-1 overflow-hidden">
        {/* LEFT: 코인 목록 버튼 + 목록 + 가격 보기 버튼 */}
        <div
          className="flex items-start justify-start border-r border-gray-800"
          style={{ width: 300 }}
        >
          <div
            className="bg-[#050816] flex flex-col"
            style={{ width: 300, height: 300 }}
          >
            {/* 상단: 코인 목록 보기 버튼 + 간단 상태 */}
            <div className="px-2 py-2 border-b border-gray-800 flex items-center gap-2">
              <button
                onClick={fetchSymbols}
                disabled={symbolsLoading}
                className="flex-1 bg-slate-700 hover:bg-slate-600 disabled:bg-slate-900 text-white text-[11px] py-1 rounded"
              >
                {symbolsLoading ? "불러오는 중..." : "코인 목록 보기"}
              </button>
            </div>

            {/* 코인 목록 (초기에는 비어 있음) */}
            <div
              style={{ height: 300 - 40, overflowY: "auto" }}
              className="text-[11px] flex flex-col"
            >
              {symbolsError && (
                <div className="p-2 text-red-400">Failed: {symbolsError}</div>
              )}

              {!symbolsError && symbols.length === 0 && !symbolsLoading && (
                <div className="p-2 text-gray-500 text-[11px]">
                  코인 목록이 없습니다. 상단의 &quot;코인 목록 보기&quot; 버튼을
                  눌러 불러오세요.
                </div>
              )}

              {symbols.map((s) => {
                const isActive =
                  selectedSymbol && s.symbol === selectedSymbol.symbol;
                const changeColor =
                  s.change24h > 0
                    ? "text-emerald-400"
                    : s.change24h < 0
                      ? "text-red-400"
                      : "text-gray-400";

                return (
                  <div
                    key={s.symbol}
                    onClick={() => setSelectedSymbol(s)}
                    style={{ border: "1px solid black" }}
                    className={`w-full h-10 px-2 cursor-pointer select-none 
                                flex items-center justify-between 
                                hover:bg-[#101827] ${
                                  isActive ? "bg-[#101827]" : ""
                                }`}
                  >
                    <div className="flex flex-col items-start">
                      <span className="text-[11px] text-gray-200 truncate max-w-[160px]">
                        {s.symbol}
                      </span>
                      <span className="text-[10px] text-gray-500 truncate max-w-[160px]">
                        {s.name}
                      </span>
                    </div>
                    <div className="flex flex-col items-end">
                      <span className="text-[11px]">
                        {s.price.toLocaleString()}
                      </span>
                      <span className={`text-[10px] ${changeColor}`}>
                        {s.change24h > 0 ? "+" : ""}
                        {Number(s.change24h).toFixed(2)}%
                      </span>
                    </div>
                  </div>
                );
              })}
            </div>

            {/* 왼쪽에서 바로 가격 보기 테스트도 할 수 있게 (옵션) */}
            <div className="px-2 py-1 border-t border-gray-800 flex gap-2 items-center">
              <button
                onClick={fetchPrice}
                disabled={priceLoading || !selectedSymbol}
                className="flex-1 bg-slate-700 hover:bg-slate-600 disabled:bg-slate-900 text-white text-[10px] py-1 rounded"
              >
                {priceLoading ? "가격 조회..." : "선택 코인 가격 보기"}
              </button>
            </div>
          </div>
        </div>

        {/* CENTER: 차트/주문(중앙 UI) — 이전 코드 그대로 사용 */}
        <main className="flex-1 flex flex-col bg-[#050816]">
          <div className="h-14 border-b border-gray-800 px-4 flex items-center justify-between text-[11px]">
            <div className="flex flex-col">
              <span className="text-[12px] text-gray-300">
                {currentSymbol.symbol}
              </span>
              <div className="flex items-baseline gap-2">
                <span className="text-2xl font-semibold text-white leading-none">
                  {currentSymbol.price.toLocaleString()}
                </span>
                <span className="text-[11px] text-gray-400">USDT</span>
                <span
                  className={`text-[11px] ${
                    currentSymbol.change24h > 0
                      ? "text-emerald-400"
                      : currentSymbol.change24h < 0
                        ? "text-red-400"
                        : "text-gray-400"
                  }`}
                >
                  {currentSymbol.change24h > 0 ? "+" : ""}
                  {Number(currentSymbol.change24h).toFixed(2)}%
                </span>
              </div>
            </div>
          </div>

          <div className="flex-1 flex flex-col">
            <div className="flex-[3] m-2 mb-1 rounded border border-gray-800 bg-gradient-to-b from-[#0f172a] to-[#020617] flex items-center justify-center">
              <span className="text-gray-500 text-xs">
                차트 영역 (실제 차트로 교체)
              </span>
            </div>
            <div className="flex-[1.2] m-2 mt-1 border border-gray-800 rounded bg-[#050816] px-3 py-2 text-[11px]">
              중앙 하단 커스텀 영역
            </div>
          </div>
        </main>

        {/* RIGHT: Order / Wallet / Ledger + 요약 패널 */}
        <div
          className="flex flex-col bg-[#050816] border-l border-gray-800"
          style={{ width: 300 }}
        >
          {/* 탭 */}
          <div className="h-9 border-b border-gray-800 flex text-[11px]">
            <button
              className={`flex-1 flex items-center justify-center border-b-2 ${
                rightTab === "order"
                  ? "border-emerald-500 text-emerald-400"
                  : "border-transparent text-gray-400"
              }`}
              onClick={() => setRightTab("order")}
            >
              Order
            </button>
            <button
              className={`flex-1 flex items-center justify-center border-b-2 ${
                rightTab === "wallet"
                  ? "border-emerald-500 text-emerald-400"
                  : "border-transparent text-gray-400"
              }`}
              onClick={() => setRightTab("wallet")}
            >
              Wallet
            </button>
            <button
              className={`flex-1 flex items-center justify-center border-b-2 ${
                rightTab === "ledger"
                  ? "border-emerald-500 text-emerald-400"
                  : "border-transparent text-gray-400"
              }`}
              onClick={() => setRightTab("ledger")}
            >
              Ledger
            </button>
          </div>

          {/* 탭 내용 */}
          <div className="flex-1 overflow-y-auto text-[11px] bg-[#050816] p-3">
            {rightTab === "order" && (
              <div className="space-y-2">
                <div className="text-gray-300 font-semibold mb-1">
                  Market Buy Test
                </div>
                <div className="grid grid-cols-2 gap-2 mb-1">
                  <div>
                    <div className="text-[10px] text-gray-400 mb-1">
                      User ID
                    </div>
                    <input
                      className="w-full bg-[#0b1020] border border-gray-700 rounded px-2 py-1 text-[11px] focus:outline-none focus:ring-1 focus:ring-emerald-500"
                      value={userId}
                      onChange={(e) => setUserId(e.target.value)}
                    />
                  </div>
                  <div>
                    <div className="text-[10px] text-gray-400 mb-1">Symbol</div>
                    <input
                      className="w-full bg-[#0b1020] border border-gray-700 rounded px-2 py-1 text-[11px]"
                      value={currentSymbol.symbol}
                      readOnly
                    />
                  </div>
                  <div>
                    <div className="text-[10px] text-gray-400 mb-1">
                      Quantity
                    </div>
                    <input
                      className="w-full bg-[#0b1020] border border-gray-700 rounded px-2 py-1 text-[11px] focus:outline-none focus:ring-1 focus:ring-emerald-500"
                      value={quantity}
                      onChange={(e) => setQuantity(e.target.value)}
                    />
                  </div>
                  <div className="flex items-end gap-1">
                    <button
                      onClick={fetchPrice}
                      disabled={priceLoading || !selectedSymbol}
                      className="flex-1 bg-slate-700 hover:bg-slate-600 disabled:bg-slate-900 text-white font-semibold text-[11px] py-1.5 rounded"
                    >
                      {priceLoading ? "가격 조회..." : "가격 보기"}
                    </button>
                    <button
                      onClick={buyMarket}
                      disabled={orderLoading || !selectedSymbol}
                      className="flex-1 bg-emerald-500 hover:bg-emerald-600 disabled:bg-emerald-900 text-white font-semibold text-[11px] py-1.5 rounded"
                    >
                      {orderLoading ? "주문 중..." : "코인 매수"}
                    </button>
                  </div>
                </div>

                <div className="h-28 bg-[#020617] border border-gray-800 rounded px-2 py-1 text-[10px] overflow-auto">
                  {latestPrice != null && (
                    <div className="mb-1 text-emerald-400">
                      현재가: {latestPrice}
                    </div>
                  )}
                  {priceError && (
                    <div className="mb-1 text-red-400">
                      가격 조회 에러: {priceError}
                    </div>
                  )}
                  {orderStatus && (
                    <div className="mb-1 text-gray-300">{orderStatus}</div>
                  )}
                  {orderError && (
                    <div className="mb-1 text-red-400">
                      주문 에러: {orderError}
                    </div>
                  )}
                  {orderResponse && (
                    <pre className="whitespace-pre-wrap text-gray-400">
                      {JSON.stringify(orderResponse, null, 2)}
                    </pre>
                  )}
                  {!latestPrice &&
                    !priceError &&
                    !orderStatus &&
                    !orderError &&
                    !orderResponse && (
                      <span className="text-gray-500">
                        가격 보기 / 코인 매수 버튼으로 API 응답을 확인해 보세요.
                      </span>
                    )}
                </div>
              </div>
            )}

            {rightTab === "wallet" && <div>Wallet 패널 (잔고 리스트 등)</div>}

            {rightTab === "ledger" && (
              <div>Ledger 패널 (체결/거래 내역 등)</div>
            )}
          </div>

          {/* 하단 요약 패널 */}
          <div className="border-t border-gray-800 bg-[#020617] px-3 py-2 text-[11px]">
            <div className="text-gray-300 font-semibold mb-1">Futures mode</div>
            <div className="flex justify-between text-gray-400 mb-1">
              <span>Equity</span>
              <span className="text-gray-100">
                {equity.toLocaleString()} USDT
              </span>
            </div>
            <div className="flex justify-between mb-1">
              <span className="text-gray-400">Floating PnL</span>
              <span
                className={
                  floatingPnl >= 0 ? "text-emerald-400" : "text-red-400"
                }
              >
                {floatingPnl >= 0 ? "+" : ""}
                {floatingPnl.toFixed(2)} USDT
              </span>
            </div>
            <div className="flex justify-between text-gray-400">
              <span>In use</span>
              <span className="text-gray-100">
                {inUse.toLocaleString()} USDT
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default TradingScreen;
