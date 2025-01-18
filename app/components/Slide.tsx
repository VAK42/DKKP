import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay } from "swiper/modules";
import { Link } from "@remix-run/react";
import "swiper/css";
import "swiper/css/pagination";
import "swiper/css/navigation";

interface Product {
  id: string;
  img: string;
  name: string;
  price: number;
  discount: number;
  disPrice?: number;
}

interface SlideProps {
  products: Product[];
}

export const Slide = ({ products }: SlideProps) => {
  return (
    <Swiper
      centeredSlides={false}
      slidesPerView={5}
      spaceBetween={20}
      autoplay={{
        delay: 2500,
        disableOnInteraction: false,
      }}
      modules={[Autoplay]}
      className="Carousel h-5/6"
    >
      {products.map((product, index) => (
        <SwiperSlide
          key={index}
          className="border-2 border-purple-500 rounded-xl overflow-hidden"
        >
          <Link to={`/${product.id}`}>
            <div className="h-2/3 border-b-2 border-purple-500 overflow-hidden">
              <img
                src={product.img}
                alt={product.name}
                className="w-full h-full"
              />
            </div>
            <div className="h-1/3 bg-purple-200">
              <div className="text-purple-500 text-xl px-2">{product.name}</div>
              {product.price == product.disPrice ? (
                <span className="text-red-500 px-2">
                  {product.price.toLocaleString("vi-VN")}
                </span>
              ) : (
                <div>
                  <span className="text-red-500 px-2">
                    {product.disPrice?.toLocaleString("vi-VN")}
                  </span>
                  <span className="line-through px-2">
                    {product.price.toLocaleString("vi-VN")}
                  </span>
                </div>
              )}
            </div>
          </Link>
        </SwiperSlide>
      ))}
    </Swiper>
  );
};