package com.sqmusicplus.plug.aria2.aria2.params.methods;

import com.sqmusicplus.plug.aria2.aria2.enums.Aria2Method;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/10/21 15:51
 */
@Data
@AllArgsConstructor
public class MulticallParam {
    String methodName;
    List<Object> params;

    public MulticallParam(Aria2Method aria2Method, List<Object> params) {
        this.methodName = aria2Method.getName();
        this.params = params;
    }



    public static class Builder {
        List<MulticallParam> list = new ArrayList<>();
        MulticallParam currentParam;

        public Builder methodName(String name) {
            this.currentParam = this.currentParam != null ? this.currentParam : new MulticallParam(name, new ArrayList<>());
            this.currentParam.setMethodName(name);
            return this;
        }
          public Builder method(Aria2Method method) {
            this.currentParam = this.currentParam != null ? this.currentParam : new MulticallParam(method, new ArrayList<>());
            this.currentParam.setMethodName(method.getName());
            return this;
        }

        public Builder addParam(Object obj){
            this.currentParam = this.currentParam != null ? this.currentParam : new MulticallParam((String) null, new ArrayList<>());
            this.currentParam.getParams().add(obj);
            return this;
        }

        public Builder complete(){
            list.add(this.currentParam);
            this.currentParam = null;
            return this;
        }
        public List<MulticallParam> build(){
            return this.list;
        }
    }
}
