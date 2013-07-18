package com.impfac

import java.math.RoundingMode

class RecordRubro3 {

    Map unidadMedidas = [UNIDAD: "07", KILOGRAMO: "01"]

    String regimenInformado = "1"
    BigDecimal tipoDocumento
    String nroDocumento
    BigDecimal item
    String posicionArancelaria
    String unidadMedida
    BigDecimal cantidad
    BigDecimal monto
    BigDecimal tipoCambio
    BigDecimal aduanaDeRegistro
    BigDecimal aduanaDeSalida
    Date fecha

    def String toString(){
        regimenInformado +
        tipoDocumento.toString().padLeft(2, '0') +
        nroDocumento.replaceAll("-", "").padLeft(16, '0') +
        item.toString().padLeft(4, '0') +
        posicionArancelaria.replaceAll(",", "").padLeft(12) +
        unidadMedidas[unidadMedida] +
        (cantidad.setScale(4, RoundingMode.HALF_UP) * 10000).toBigInteger().toString().padLeft(13, '0') +
        (monto.setScale(2, RoundingMode.HALF_UP) * 100).toBigInteger().toString().padLeft(15, '0') +
        (tipoCambio.setScale(6, RoundingMode.HALF_UP) * 1000000).toBigInteger().toString().padLeft(10, '0') +
        aduanaDeRegistro.toString().padLeft(3, '0') +
        aduanaDeSalida.toString().padLeft(3, '0') +
        fecha.format('yyyyMMdd')
    }
}
