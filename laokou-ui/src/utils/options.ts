import { DictValueEnumObj } from "@/components/DictTag";
import { ProSchemaValueEnumObj, ProSchemaValueEnumType } from "@ant-design/pro-components";

export function getValueEnumLabel(options: DictValueEnumObj | ProSchemaValueEnumObj, val: string | number | undefined, defaultValue?: string) {
    if (val !== undefined) {
       const data = options[val] as ProSchemaValueEnumType;
       if(data) {
        return data.text;
       }
    }
    return defaultValue?defaultValue:val;
}
