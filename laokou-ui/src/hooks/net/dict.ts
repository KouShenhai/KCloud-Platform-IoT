import { getDictValueEnum } from "@/services/system/dict";

export function useDictEnum(name: string)
{
    const data = getDictValueEnum(name);
    return data;
}